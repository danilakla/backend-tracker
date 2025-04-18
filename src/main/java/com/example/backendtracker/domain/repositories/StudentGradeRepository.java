package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.StudentGrade;
import com.example.backendtracker.domain.repositories.mapper.StudentGradeSummaryRowMapper;
import com.example.backendtracker.entities.teacher.dto.StudentGradeSummary;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGradeRepository extends CrudRepository<StudentGrade, Integer> {

    List<StudentGrade> findAllByIdClass(int id);

    List<StudentGrade> findAllByIdClassInAndAndIdStudentIn(List<Integer> idClass, List<Integer> studentId);

    @Query(value = "WITH AttestationClass AS (\n" +
            "    SELECT \n" +
            "        id_class,\n" +
            "        id_class_hold,\n" +
            "        date_creation\n" +
            "    FROM Classes\n" +
            "    WHERE id_class = :attestationClassId\n" +
            "        AND is_attestation = TRUE\n" +
            ")\n" +
            "SELECT \n" +
            "    sg.id_student AS studentId,\n" +
            "    AVG(CASE WHEN sg.grade > 0 THEN sg.grade ELSE NULL END) AS avgGrade,\n" +
            "    COUNT(CASE WHEN sg.attendance = 3 THEN 1 END) AS attendanceCount,\n" +
            "    SUM(CASE WHEN sg.is_pass_lab = TRUE THEN 1 ELSE 0 END) AS passLabCount\n" +
            "FROM Classes c\n" +
            "JOIN StudentGrades sg ON c.id_class = sg.id_class\n" +
            "JOIN Students st ON sg.id_student = st.id_student\n" +
            "JOIN AttestationClass ac ON c.id_class_hold = ac.id_class_hold\n" +
            "WHERE c.id_class_hold = :idClassHold\n" +
            "    AND c.is_attestation = FALSE\n" +
            "    AND c.date_creation < ac.date_creation\n" +
            "GROUP BY sg.id_student\n" +
            "ORDER BY sg.id_student;", rowMapperClass = StudentGradeSummaryRowMapper.class)
    List<StudentGradeSummary> findStudentGradeSummaryByClassHold(@Param("idClassHold") int idClassHold,@Param("attestationClassId") int attestationClassId);
    @Modifying()
    @Query("update studentgrades set attendance=1 where id_class=:idclass and attendance=0")
    public  void updateAttanceStartqr(Integer idclass);
}
