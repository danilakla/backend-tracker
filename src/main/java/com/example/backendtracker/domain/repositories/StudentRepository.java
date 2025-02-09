package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.UserRole;
import com.example.backendtracker.domain.repositories.mapper.StudentWithLogin;
import com.example.backendtracker.domain.repositories.mapper.StudentWithLoginMapper;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    @Query(value = "SELECT * FROM Students WHERE id_account = :id_account")
    Optional<Student> findByIdAccount(@Param("id_account") Integer id_account);


    Optional<Student> findStudentByKeyStudentParents(String parentsKey);

    List<Student> findAllByIdSubgroup(Integer idSubgroup);

    @Query(value = "SELECT \n" +
            "    s.id_student,\n" +
            "    s.flp_name,\n" +
            "    s.key_student_parents,\n" +
            "    ua.login,\n" +
            "    s.id_subgroup,\n" +
            "    s.id_account\n" +
            "FROM \n" +
            "    Students s\n" +
            "JOIN \n" +
            "    UserAccounts ua ON s.id_account = ua.id_account\n" +
            "WHERE \n" +
            "    s.id_subgroup = :idSubgroup", rowMapperClass = StudentWithLoginMapper.class)
    List<StudentWithLogin> findAllByIdSubgroupWithLogin(Integer idSubgroup);

    List<Student> findAllByIdSubgroupIn(List<Integer> idSubgroup);

    void deleteByIdAccount(Integer idAccount);
}