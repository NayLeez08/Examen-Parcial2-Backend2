package com.examen.backend2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearProyectoRequest(
        @NotBlank
        @Size(max = 100)
        String nombre,

        String descripcion
) {}