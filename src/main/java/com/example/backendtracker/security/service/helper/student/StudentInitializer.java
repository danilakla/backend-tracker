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
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
                        student -> new GroupAndSpecialtyKey((student).studentExcelDto().numberOfGroup(), specialtyIdMap.get(student.studentExcelDto().specialty()))
                ));
    }

    public void initStudent(List<StudentExcelDto> studentExcelDtos, Integer idDean) {

        Map<GroupAndSpecialtyKey, List<StudentWithCredentials>> map = convertListToMapWithCredentials(studentExcelDtos);

        // Вывод карты на экран
        map.forEach((key, value) -> {

            System.out.println("Group: " + key.getNumberOfGroup() + ", Specialty ID: " + key.getIdSpecialty() + " has students:" + idDean + "dean id");
            //todo
            subgroupRepository.save(Subgroup.builder().admissionDate(null).idDean(idDean).idSpecialty(key.getIdSpecialty()).subgroupNumber(key.getNumberOfGroup()).build());
            value.forEach(student ->
            {
                //todo
                Integer AccountId = userAccountService.createUserAccount(UserRegistrationRequestDTO.builder().role("STUDENT").login(student.login()).password(student.password()).build());
                System.out.println(" - " + student.studentExcelDto().name() + " (Login: " + student.login() + ", Password: " + student.password() + ", Parent key: +" + student.parentKey() + ") ");
                //todo 
                studentRepository.save(Student.builder().build());
            });
        });
    }

}

