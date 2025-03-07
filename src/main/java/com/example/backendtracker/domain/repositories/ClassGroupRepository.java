package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassGroup;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupForStudentRowMapper;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapDTO;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapForStudentDTO;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupRowMapper;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassGroupRepository extends CrudRepository<ClassGroup, Integer> {

    @Modifying
    @Query("UPDATE ClassGroups SET id_teacher = :newTeacherId WHERE id_teacher = :oldTeacherId")
    void updateTeacherId(@Param("newTeacherId") int newTeacherId, @Param("oldTeacherId") int oldTeacherId);

    @Modifying
    @Query("UPDATE ClassGroups SET id_dean = :newDeanId WHERE id_dean = :oldDeanId")
    void updateByDeanId(@Param("newDeanId") int newDeanId, @Param("oldDeanId") int oldDeanId);

    @Query(value = "select cg.*,sb.name, cf.format_name, tc.flp_name from ClassGroups cg JOIN Subjects sb ON sb.id_subject = cg.id_subject JOIN ClassFormats cf ON cf.id_class_format = cg.id_class_format  JOIN Teachers tc ON tc.id_teacher = cg.id_teacher WHERE cg.id_dean = :id_dean", rowMapperClass = ClassGroupRowMapper.class)
    List<ClassGroupMapDTO> findAllByIdDean(@Param("id_dean") int id);

    @Query(value = "select cg.*,sb.name, cf.format_name, tc.flp_name from ClassGroups cg JOIN Subjects sb ON sb.id_subject = cg.id_subject JOIN ClassFormats cf ON cf.id_class_format = cg.id_class_format  JOIN Teachers tc ON tc.id_teacher = cg.id_teacher WHERE cg.id_teacher = :id_teacher", rowMapperClass = ClassGroupRowMapper.class)
    List<ClassGroupMapDTO> findAllByIdTeacher(@Param("id_teacher") int id);


    @Query(value = "SELECT cg.id_class_group AS idClassGroup, cgs.id_class_group_to_subgroup as idClassGroupToSubgroup, cgs.id_subgroup as idSubgroup,  cgs.id_class_hold as idHold, cg.description AS description, s.name AS subjectName, cf.format_name AS formatName, t.flp_name AS teacherName " +
            "FROM ClassGroups cg " +
            "JOIN Subjects s ON cg.id_subject = s.id_subject " +
            "JOIN ClassFormats cf ON cg.id_class_format = cf.id_class_format " +
            "JOIN Teachers t ON cg.id_teacher = t.id_teacher " +
            "JOIN ClassGroupsToSubgroups cgs ON cg.id_class_group = cgs.id_class_group " +
            "WHERE cgs.id_subgroup = :idSubgroup", rowMapperClass = ClassGroupForStudentRowMapper.class)
    List<ClassGroupMapForStudentDTO> findClassGroupsMapBySubgroupId(@Param("idSubgroup") Integer idSubgroup);

    Optional<ClassGroup> findByIdTeacherAndIdClassFormatAndAndIdSubject(int teacherId, int classFormatId, int subjectId);

    List<ClassGroup> findAllByIdSubjectAndIdDean(Integer subjectId, Integer deanId);
}