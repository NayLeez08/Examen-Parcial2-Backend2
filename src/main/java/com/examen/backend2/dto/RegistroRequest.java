package com.examen.backend2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank
        @Size(max = 50)
        String username,

        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {}