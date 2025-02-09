package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.repositories.mapper.TeacherWithLogin;
import com.example.backendtracker.domain.repositories.mapper.TeacherWithLoginMapper;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    @Query(value = "SELECT * FROM Teachers WHERE id_account = :id_account")
    Optional<Teacher> findByIdAccount(@Param("id_account") Integer id_account);

    List<Teacher> findAllByIdUniversity(Integer id_university);

    @Query(value = "SELECT \n" +
            "    t.id_teacher,\n" +
            "    t.id_university,\n" +
            "    t.flp_name,\n" +
            "    t.id_account,\n" +
            "    ua.login\n" +
            "FROM \n" +
            "    Teachers t\n" +
            "JOIN \n" +
            "    UserAccounts ua ON t.id_account = ua.id_account\n" +
            "WHERE \n" +
            "    t.id_university = :idUniversity", rowMapperClass = TeacherWithLoginMapper.class)
    List<TeacherWithLogin> findAllByIdUniversityLogin(Integer idUniversity);

    List<Teacher> findAllByIdTeacherIn(List<Integer> teacherIds);

    Optional<Teacher> findByIdUniversityAndIdTeacher(Integer id_university, Integer id_teacher);
}