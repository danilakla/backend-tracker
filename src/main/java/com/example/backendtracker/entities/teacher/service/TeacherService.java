package com.example.backendtracker.entities.teacher.service;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapDTO;
import com.example.backendtracker.entities.admin.dto.ClassGroupDto;
import com.example.backendtracker.entities.admin.dto.ClassGroupInfo;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.teacher.dto.*;
import com.example.backendtracker.reliability.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class TeacherService {
    private final ClassGroupRepository classGroupRepository;
    private final ClassGroupsToSubgroupsRepository classGroupsToSubgroupsRepository;
    private final CommonService commonService;
    private final ClassRepository classRepository;
    private final StudentGradeRepository studentGradeRepository;
    private final AttestationStudentGradeRepository attestationStudentGradeRepository;
    private final ClassGroupsHoldRepository classGroupsHoldRepository;
    private final JdbcTemplate jdbcTemplate;

    public List<ClassGroupMapDTO> getListClassGroups(Integer teacherId) {
        return classGroupRepository.findAllByIdTeacher(teacherId);

    }

    public void deleteClass(Integer idClass) {
        classRepository.deleteById(idClass);
    }

    private Classes createClass(Integer idHole, Boolean isAttestation) {
        return classRepository.save(Classes.builder().idClassHold(idHole).isAttestation(isAttestation).dateCreation(LocalDate.now()).build());
    }


    private List<StudentGrade> createStudentGrade(List<Integer> studentsId, Integer classesId) {
        createStudentGrateViaBatch(studentsId, classesId, "INSERT INTO StudentGrades (id_student,id_class,attendance) VALUES (?,?,?)");
        return studentGradeRepository.findAllByIdClass(classesId);
    }

    private void createStudentGradeAttestation(List<Integer> studentsId, Integer classesId) {
        if (!studentsId.isEmpty()) {

            createStudentGrateAttestationViaBatch(studentsId, classesId, "INSERT INTO AttestationStudentGrades (id_student,id_class) VALUES (?,?)");
//        return studentGradeRepository.findAllByIdClass(classesId);
        }
    }

    public void acceptAttendance(Integer studentGrate, Integer attendanceCode) {
        StudentGrade studentGrade = studentGradeRepository.findById(studentGrate).orElseThrow();
        studentGrade.setAttendance(attendanceCode);
        studentGradeRepository.save(studentGrade);
    }

    @Transactional
    public void generateStudentGrateForAttestation(Integer holdId, List<Integer> studentIds) {
        try {
            Classes classes = createClass(holdId, true);
            createStudentGradeAttestation(studentIds, classes.getIdClass());
//            return ClassInfoDto.builder().classes(classes).studentGrades(studentGrades).build();


        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ClassInfoDto generateStudentGrateForNewClass(CreateClassInfo createClassInfo) {
        try {
            Classes classes = createClass(createClassInfo.holdId(), false);
            List<StudentGrade> studentGrades = createStudentGrade(createClassInfo.studentship(), classes.getIdClass());
            return ClassInfoDto.builder().classes(classes).studentGrades(studentGrades).build();


        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void createStudentGrateViaBatch(List<Integer> ids, Integer classId, String sql) {

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Integer idStudentGroup = ids.get(i);

                        ps.setInt(1, idStudentGroup);
                        ps.setInt(2, classId);
                        ps.setInt(3, 0);
                    }

                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                }

        );


    }

    @Transactional
    public List<AttestationStudentGrade> calculateAvgForAttestationProp(
            AttestationCalculationDto attestationCalculationDto) {

        List<StudentGradeSummary> studentGrades = studentGradeRepository.findStudentGradeSummaryByClassHold(attestationCalculationDto.holdId());
        List<AttestationStudentGrade> attestationStudentGrades = attestationStudentGradeRepository.findAllByIdClassAndIdStudentIn(
                attestationCalculationDto.classId(),
                attestationCalculationDto.studentId()
        );

        Map<Integer, StudentGradeSummary> studentGradeSummaryMap = studentGrades.stream().collect(Collectors.toMap(StudentGradeSummary::getStudentId, el -> el));

        List<AttestationStudentGrade> updatedGrades = attestationStudentGrades.stream()
                .peek(attestationGrade -> {
                    StudentGradeSummary summary = studentGradeSummaryMap.get(attestationGrade.getIdStudent());
                    if (summary != null) {
                        attestationGrade.setMaxCountLab(attestationCalculationDto.maxLabCount());
                        attestationGrade.setCurrentCountLab(summary.getPassLabCount());
                        attestationGrade.setAvgGrade(summary.getAvgGrade());
                        attestationGrade.setHour(((attestationCalculationDto.countClassThatNotAttestation() - summary.getAttendanceCount()) * attestationCalculationDto.timeOfOneClass()) / 60);
                    }
                })
                .collect(Collectors.toList());
        attestationStudentGradeRepository.saveAll(updatedGrades);
        return updatedGrades;
    }


    private void createStudentGrateAttestationViaBatch(List<Integer> ids, Integer classId, String sql) {

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Integer idStudentGroup = ids.get(i);

                        ps.setInt(1, idStudentGroup);
                        ps.setInt(2, classId);
                    }

                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                }

        );


    }

    public StudentGrade updateStudentGrade(UpdateStudentGrade updateStudentGrade) {
        StudentGrade studentGrade = studentGradeRepository.findById(updateStudentGrade.idStudentGrate()).orElseThrow(() -> new BadRequestException("Student grade not found"));
        studentGrade.setAttendance(updateStudentGrade.attendance());
        studentGrade.setDescription(updateStudentGrade.description());
        studentGrade.setGrade(updateStudentGrade.grade());
        studentGrade.setIsPassLab(updateStudentGrade.isPassLab());
        return studentGradeRepository.save(studentGrade);
    }

    public AttestationStudentGrade updateAttestationStudentGrade(UpdateAttestationStudentGrade updateStudentGrade) {
        AttestationStudentGrade attestationStudentGrade = attestationStudentGradeRepository.findById(updateStudentGrade.idAttestationStudentGrades()).orElseThrow(() -> new BadRequestException("Student attestation grade not found"));
        attestationStudentGrade.setAvgGrade(updateStudentGrade.avgGrade());
        attestationStudentGrade.setHour(updateStudentGrade.hour());
        attestationStudentGrade.setCurrentCountLab(updateStudentGrade.currentCountLab());
        attestationStudentGrade.setMaxCountLab(updateStudentGrade.maxCountLab());
        return attestationStudentGradeRepository.save(attestationStudentGrade);
    }


    public ClassGroupDto getClassGroup(Integer classGroupId, Integer teacherId) {
        ClassGroupInfo classGroupInfo = commonService.getClassGroup(classGroupId);
        hasBelongToTeacher(classGroupInfo.classGroup().getIdTeacher(), teacherId);

        List<ClassGroupsToSubgroups> classGroupsToSubgroups = classGroupsToSubgroupsRepository.findAllByIdClassGroup(classGroupInfo.classGroup().getIdClassGroup());
        List<Boolean> listAttested= classGroupsHoldRepository.findByIdClassHoldIn(classGroupsToSubgroups.stream().map(ClassGroupsToSubgroups::getIdClassHold).toList()).stream().map(ClassGroupsHold::getHasApplyAttestation).toList();

        return ClassGroupDto.builder().hasApplyAttestation(listAttested).classGroup(classGroupInfo).subgroupsId(classGroupsToSubgroups).build();
    }

    private void hasBelongToTeacher(Integer entityIdTeacher, Integer requestedTeacherId) {
        if (!Objects.equals(entityIdTeacher, requestedTeacherId))
            throw new BadRequestException("The dean does not belong to the requested dean");
    }
}
