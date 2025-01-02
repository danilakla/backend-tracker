package com.example.backendtracker.qrmanager.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.TimeToLive;

@Builder
public record ReviewDto(int classId, Long expiration) {

}
