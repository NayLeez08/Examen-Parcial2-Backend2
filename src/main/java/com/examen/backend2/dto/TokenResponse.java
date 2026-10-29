package com.examen.backend2.dto;

public record TokenResponse(
        String token,
        String tipo,
        String username
) {}