package com.example.backendtracker.entities.teacher.service;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.entities.admin.dto.ClassGroupDto;
import com.example.backendtracker.entities.admin.dto.ClassGroupInfo;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.teacher.dto.ClassInfoDto;
import com.example.backendtracker.entities.teacher.dto.CreateClassInfo;
import com.example.backendtracker.entities.teacher.dto.TableViewDto;
import com.example.backendtracker.reliability.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
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
    private final JdbcTemplate jdbcTemplate;

    public List<ClassGroup> getListClassGroups(Integer teacherId) {
        return classGroupRepository.findAllByIdTeacher(teacherId);

    }

    private Classes createClass(Integer classGroupToSubgroupId) {
        return classRepository.save(Classes.builder().idClassGroupToSubgroup(classGroupToSubgroupId).build());
    }


    private List<StudentGrade> createStudentGrade(List<Integer> studentsId, Integer classesId) {
        createStudentGrateViaBatch(studentsId, classesId, "INSERT INTO StudentGrades (id_student,id_class,attendance) VALUES (?,?,?)");
        return studentGradeRepository.findAllByIdClass(classesId);
    }

    public ClassInfoDto generateStudentGrateForNewClass(CreateClassInfo createClassInfo) {
        Classes classes = createClass(createClassInfo.classGroupToSubgroupId());
        List<StudentGrade> studentGrades = createStudentGrade(createClassInfo.studentship(), classes.getIdClass());
        return ClassInfoDto.builder().classes(classes).studentGrades(studentGrades).build();
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
                        ps.setBoolean(3, false);
                    }

                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                }

        );


    }


    public ClassGroupDto getClassGroup(Integer classGroupId, Integer teacherId) {
        ClassGroupInfo classGroupInfo = commonService.getClassGroup(classGroupId);
        hasBelongToTeacher(classGroupInfo.classGroup().getIdTeacher(), teacherId);

        List<ClassGroupsToSubgroups> classGroupsToSubgroups = classGroupsToSubgroupsRepository.findAllByIdClassGroup(classGroupInfo.classGroup().getIdClassGroup());
        return ClassGroupDto.builder().classGroup(classGroupInfo).subgroupsId(classGroupsToSubgroups).build();
    }

    private void hasBelongToTeacher(Integer entityIdTeacher, Integer requestedTeacherId) {
        if (!Objects.equals(entityIdTeacher, requestedTeacherId))
            throw new BadRequestException("The dean does not belong to the requested dean");
    }
}