package com.example.backendtracker.entities.teacher.dto;

import java.util.List;

public record HoldStudents(Integer holdId, List<String> studentIds) {}