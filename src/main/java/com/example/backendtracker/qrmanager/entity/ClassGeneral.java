package com.example.backendtracker.qrmanager.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Data
@RedisHash("classgeneral")
public class ClassGeneral implements Serializable {
    @Id
    private int classId;
    @TimeToLive
    private Long expiration;
}