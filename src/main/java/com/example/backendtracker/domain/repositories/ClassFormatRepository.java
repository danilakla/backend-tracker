package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassFormat;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassFormatRepository extends CrudRepository<ClassFormat, Integer> {
    @Modifying
    @Query("UPDATE ClassFormats SET id_dean = :newDeanId WHERE id_dean = :oldDeanId")
    void updateDeanId(@Param("newDeanId") int newDeanId, @Param("oldDeanId") int oldDeanId);
}
