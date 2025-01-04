package com.example.backendtracker.entities.common.dto;

import java.util.List;

public record ClassesTableDto(Integer holdId, List<Integer> groupsId) {
}
