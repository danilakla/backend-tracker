package com.example.backendtracker.qrmanager.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@Builder
@RedisHash("studentreview")
public class StudentReview implements Serializable {
    @Id
    private int studentGradeId;
    @Indexed
    private int classId;
    @TimeToLive
    private Long expiration;
}