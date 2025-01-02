package com.example.backendtracker.qrmanager.repository;

import com.example.backendtracker.qrmanager.entity.ClassGeneral;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassGeneralRepository extends CrudRepository<ClassGeneral, Integer> {
}