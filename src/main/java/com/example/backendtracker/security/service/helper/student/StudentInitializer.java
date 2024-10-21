package com.example.backendtracker.security.service.helper.student;


import com.example.backendtracker.domain.models.Specialty;
import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.Subgroup;
import com.example.backendtracker.domain.repositories.SpecialtyRepository;
import com.example.backendtracker.domain.repositories.StudentRepository;
import com.example.backendtracker.domain.repositories.SubgroupRepository;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.security.service.data.StudentExcelDto;
import com.example.backendtracker.security.util.LoginGenerator;
import com.example.backendtracker.security.util.PasswordGenerator;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentInitializer {

    private final UserAccountService userAccountService;
    private final SpecialtyRepository specialtyRepository;
    private final SubgroupRepository subgroupRepository;
    private final StudentRepository studentRepository;
    private final JdbcTemplate jdbcTemplate;

    private Map<String, Integer> getSpecialtyIdsFromDatabase(List<String> specialtyNames) {
        List<Specialty> specialties = specialtyRepository.findAllByNameIn(specialtyNames);

        return specialties.stream().collect(Collectors.toMap(Specialty::getName, Specialty::getIdSpecialty));
    }

    public Map<GroupAndSpecialtyKey, List<StudentWithCredentials>> convertListToMapWithCredentials(List<StudentExcelDto> students) {
        // Собираем уникальные названия специальностей
        List<String> specialtyNames = students.stream()
                .map(StudentExcelDto::specialty)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Integer> specialtyIdMap = getSpecialtyIdsFromDatabase(specialtyNames);

        return students.stream()
                .map(student ->
                        StudentWithCredentials.builder()
                                .login(LoginGenerator.generateLogin(student))
                                .password(PasswordGenerator.generatePassword())
                                .parentKey(LoginGenerator.generateLogin(student) + PasswordGenerator.generatePassword())
                                .studentExcelDto(student).build()
                )
                .collect(Collectors.groupingBy(
                        student -> new GroupAndSpecialtyKey(student.studentExcelDto().numberOfGroup(), specialtyIdMap.get(student.studentExcelDto().specialty()))
                ));
    }

    @Transactional
    public void initStudent(List<StudentExcelDto> studentExcelDtos, Integer idDean) {

        // Step 1: Group students by group and specialty
        Map<GroupAndSpecialtyKey, List<StudentWithCredentials>> map = convertListToMapWithCredentials(studentExcelDtos);

        // Step 2: Batch insert subgroups and collect generated subgroup IDs
        List<Integer> subgroupIds = batchInsertSubgroups(map, idDean);

        // Step 3: Batch insert students with their corresponding account IDs
        batchInsertStudents(map, subgroupIds);
    }

    private List<Integer> batchInsertSubgroups(Map<GroupAndSpecialtyKey, List<StudentWithCredentials>> map, Integer idDean) {
        String sql = "INSERT INTO subgroup (admission_date, id_dean, id_specialty, subgroup_number) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        List<GroupAndSpecialtyKey> groupKeys = new ArrayList<>(map.keySet());

        jdbcTemplate.batchUpdate(
                connection -> connection.prepareStatement(sql, new String[]{"id_subgroup"}), // PreparedStatementCreator
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        GroupAndSpecialtyKey key = groupKeys.get(i);
                        ps.setObject(1, LocalDate.now());
                        ps.setInt(2, idDean);
                        ps.setInt(3, key.getIdSpecialty());
                        ps.setString(4, key.getNumberOfGroup());
                    }

                    @Override
                    public int getBatchSize() {
                        return groupKeys.size();
                    }
                },
                keyHolder
        );

        return keyHolder.getKeyList().stream()
                .map(keyMap -> (Integer) keyMap.get("id_subgroup"))
                .collect(Collectors.toList());
    }

    private void batchInsertStudents(Map<GroupAndSpecialtyKey, List<StudentWithCredentials>> map, List<Integer> subgroupIds) {
        String sql = "INSERT INTO student (flp_name, login, password, id_account, key_student_parents, id_subgroup) VALUES (?, ?, ?, ?, ?, ?)";

        List<StudentWithCredentials> allStudents = new ArrayList<>();
        List<Integer> allSubgroupIds = new ArrayList<>();

        // Flatten the map to get all students with their respective subgroup IDs
        List<GroupAndSpecialtyKey> groupKeys = new ArrayList<>(map.keySet());
        for (int i = 0; i < groupKeys.size(); i++) {
            GroupAndSpecialtyKey key = groupKeys.get(i);
            List<StudentWithCredentials> students = map.get(key);
            allStudents.addAll(students);

            // Assign the corresponding subgroup ID to all students in the group
            for (int j = 0; j < students.size(); j++) {
                allSubgroupIds.add(subgroupIds.get(i));
            }
        }

        // Batch insert user accounts and retrieve account IDs
        List<UserRegistrationRequestDTO> registrationRequests = allStudents.stream()
                .map(student -> UserRegistrationRequestDTO.builder().password(student.password()).role("STUDENT").login(student.login()).build())
                .collect(Collectors.toList());

        List<Integer> accountIds = userAccountService.createUserAccountsInBatch(registrationRequests);

        // Batch insert students with the generated account IDs and subgroup IDs
        jdbcTemplate.batchUpdate(sql, // PreparedStatementCreator
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        StudentWithCredentials student = allStudents.get(i);
                        ps.setString(1, student.studentExcelDto().lastname() + "_" + student.studentExcelDto().name() + "_" + student.studentExcelDto().surname());
                        ps.setString(2, student.login());
                        ps.setString(3, student.password());
                        ps.setInt(4, accountIds.get(i)); // Use generated account ID
                        ps.setString(5, student.parentKey());
                        ps.setInt(6, allSubgroupIds.get(i)); // Use generated subgroup ID
                    }

                    @Override
                    public int getBatchSize() {
                        return allStudents.size();
                    }
                }
        );
    }

}

