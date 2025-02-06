package com.example.backendtracker.attestation.controller;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

public record ExpirationTimeInSeconds(Integer time) {
}
