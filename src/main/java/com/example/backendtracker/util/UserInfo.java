package com.example.backendtracker.util;

import lombok.Builder;

@Builder
public record UserInfo(String lastname, String  name, String surname) {
}
