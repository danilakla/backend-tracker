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

    @Query(value = """
    WITH StudentUnattested AS (
        SELECT 
            s.id_student,
            cgts.id_class_group,
            COUNT(asg.id_attestation_student_grades) AS unattested_count
        FROM Students s
        JOIN Subgroups sg ON s.id_subgroup = sg.id_subgroup
        JOIN ClassGroupsToSubgroups cgts ON sg.id_subgroup = cgts.id_subgroup
        LEFT JOIN Classes c ON cgts.id_class_hold = c.id_class_hold
        LEFT JOIN AttestationStudentGrades asg 
            ON asg.id_class = c.id_class 
            AND asg.id_student = s.id_student 
            AND asg.is_attested = false
        WHERE sg.id_dean = :deanId
        GROUP BY s.id_student, cgts.id_class_group
    )
    SELECT
        s.id_student,
        s.flp_name AS student_name,
        sg.id_subgroup,
        sg.subgroup_number,
        sg.admission_date,
        cg.id_class_group,
        cg.description AS class_group_description,
        subjects.name as sub_name,
        classformats.format_name as frm_name,
        teachers.flp_name as teach_nname,
        cgts.id_class_hold,
        COALESCE(su.unattested_count, 0) AS unattested_count
    FROM Students s
    JOIN Subgroups sg ON s.id_subgroup = sg.id_subgroup
    JOIN ClassGroupsToSubgroups cgts ON sg.id_subgroup = cgts.id_subgroup
    JOIN ClassGroups cg ON cgts.id_class_group = cg.id_class_group
    JOIN subjects on cg.id_subject = subjects.id_subject
    JOIN classformats on cg.id_class_format = classformats.id_class_format
    JOIN teachers  on cg.id_teacher = teachers.id_teacher
    LEFT JOIN StudentUnattested su 
        ON s.id_student = su.id_student 
        AND cg.id_class_group = su.id_class_group
    WHERE sg.id_dean = :deanId
    """,
            rowMapperClass = StudentRowMapper.class)
    List<StudentDTO> findAllNotAttestedStudentWhoHasMoreThen2NotAttestationByDeanId(Integer deanId);

}
