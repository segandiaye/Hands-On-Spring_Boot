package com.example.securitydemo.dto;

public record LoginRequest(
        String username,
        String password
) {
}
