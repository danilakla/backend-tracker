package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Admin;
import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.repositories.mapper.*;
import com.example.backendtracker.entities.admin.dto.TeacherHolds;
import com.example.backendtracker.entities.dean.dto.GroupedResultDTO;
import com.example.backendtracker.entities.dean.dto.StudentDTO;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DeanRepository extends CrudRepository<Dean, Integer> {
    @Query(value = "SELECT * FROM Deans WHERE id_account = :id_account")
    Optional<Dean> findByIdAccount(@Param("id_account") Integer id_account);

    List<Dean> findAllByIdUniversity(Integer id_university);

    @Query(value = "SELECT \n" +
            "    cg.id_teacher AS teacher_id,\n" +
            "    ARRAY_AGG(DISTINCT cgts.id_class_hold) AS hold_ids\n" +
            "FROM Deans d\n" +
            "JOIN ClassGroups cg ON d.id_dean = cg.id_dean\n" +
            "JOIN ClassGroupsToSubgroups cgts ON cg.id_class_group = cgts.id_class_group\n" +
            "JOIN classgroupshold clhold ON cgts.id_class_hold = clhold.id_class_hold\n" +
            "WHERE d.id_dean = :deanId\n" +
            "AND  clhold.has_apply_attestation = TRUE\n" +
            "GROUP BY cg.id_teacher\n" +
            "ORDER BY cg.id_teacher", rowMapperClass = TeacherHoldsRowMapper.class)
    List<TeacherHolds> findTeacherAndTheirHoldsId(@Param("deanId") Integer deanId);


    @Query(value = """
            SELECT cgts.id_class_hold AS hold_id, 
                   s.id_student AS student_id
            FROM ClassGroupsToSubgroups cgts
            JOIN Students s ON cgts.id_subgroup = s.id_subgroup
            WHERE cgts.id_class_hold IN (:holdIds)
            """, rowMapperClass = HoldRowMapper.class)
    List<HoldStudentProjection> findStudentsByHoldIds(@Param("holdIds") List<Integer> holdIds);

    @Query(value = "SELECT \n" +
            "    d.id_dean,\n" +
            "    d.flp_name,\n" +
            "    d.faculty,\n" +
            "    d.id_university,\n" +
            "    d.id_account,\n" +
            "    ua.login\n" +
            "FROM \n" +
            "    Deans d\n" +
            "JOIN \n" +
            "    UserAccounts ua ON d.id_account = ua.id_account\n" +
            "WHERE \n" +
            "    d.id_university = :idUniversity", rowMapperClass = DeanWithLoginMapper.class)
    List<DeanWithLogin> findAllByIdUniversityWithLogin(Integer idUniversity);

    @Query(value = "WITH FilteredStudents AS (\n" +
            "    SELECT s.id_student\n" +
            "    FROM Students s\n" +
            "    JOIN Subgroups sg ON s.id_subgroup = sg.id_subgroup\n" +
            "    JOIN AttestationStudentGrades asg ON s.id_student = asg.id_student\n" +
            "    WHERE sg.id_dean = :deanId AND asg.is_attested = true\n" +
            "    GROUP BY s.id_student\n" +
            "    HAVING COUNT(asg.id_attestation_student_grades) > 2\n" +
            ")\n" +
            "SELECT\n" +
            "    s.id_student,\n" +
            "    s.flp_name AS student_name,\n" +
            "    sg.id_subgroup,\n" +
            "    sg.subgroup_number,\n" +
            "    sg.admission_date,\n" +
            "    cg.id_class_group,\n" +
            "    cg.description AS class_group_description,\n" +
            "    cg.id_subject,\n" +
            "    cg.id_class_format,\n" +
            "    cg.id_teacher\n" +
            "FROM FilteredStudents fs\n" +
            "JOIN Students s ON fs.id_student = s.id_student\n" +
            "JOIN Subgroups sg ON s.id_subgroup = sg.id_subgroup\n" +
            "JOIN ClassGroupsToSubgroups cgts ON sg.id_subgroup = cgts.id_subgroup\n" +
            "JOIN ClassGroups cg ON cgts.id_class_group = cg.id_class_group\n" +
            "JOIN AttestationStudentGrades asg ON s.id_student = asg.id_student\n" +
            "JOIN Classes c ON asg.id_class = c.id_class\n" +
            "WHERE asg.is_attested = true\n" +
            "AND c.id_class_hold = cgts.id_class_hold\n" +
            "AND sg.id_dean = :deanId;", rowMapperClass = StudentRowMapper.class)
    List<StudentDTO> findAllNotAttestedStudentWhoHasMoreThen2NotAttestationByDeanId(Integer deanId);

}
