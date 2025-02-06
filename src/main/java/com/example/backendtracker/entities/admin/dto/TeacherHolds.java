package com.example.backendtracker.entities.admin.dto;

import java.util.List;

public record TeacherHolds(Integer teacherId, List<Integer> holdIds) {}