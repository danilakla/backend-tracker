package com.example.backendtracker.qrmanager.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Data
@Builder
@RedisHash("review")
public class Review implements Serializable {
    @Id
    private int classId;

    @TimeToLive
    private Long expiration;
}