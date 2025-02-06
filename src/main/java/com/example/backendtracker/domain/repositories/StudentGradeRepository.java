package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.StudentGrade;
import com.example.backendtracker.domain.repositories.mapper.StudentGradeSummaryRowMapper;
import com.example.backendtracker.entities.teacher.dto.StudentGradeSummary;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGradeRepository extends CrudRepository<StudentGrade, Integer> {

    List<StudentGrade> findAllByIdClass(int id);

    List<StudentGrade> findAllByIdClassInAndAndIdStudentIn(List<Integer> idClass, List<Integer> studentId);

    @Query(value = "SELECT " +
            "sg.id_student AS studentId, " +
            "AVG(CASE WHEN sg.grade > 0 THEN sg.grade ELSE NULL END) AS avgGrade, " +
            "COUNT(CASE WHEN sg.attendance IN (4) THEN 1 END) AS attendanceCount, " +
            "SUM(CASE WHEN sg.is_pass_lab = TRUE THEN 1 ELSE 0 END) AS passLabCount " +
            "FROM ClassGroupsToSubgroups cgt " +
            "JOIN Subgroups s ON cgt.id_subgroup = s.id_subgroup " +
            "JOIN Students st ON s.id_subgroup = st.id_subgroup " +
            "JOIN StudentGrades sg ON st.id_student = sg.id_student " +
            "WHERE cgt.id_class_hold = :idClassHold " +
            "GROUP BY sg.id_student", rowMapperClass = StudentGradeSummaryRowMapper.class)
    List<StudentGradeSummary> findStudentGradeSummaryByClassHold(@Param("idClassHold") int idClassHold);

}
