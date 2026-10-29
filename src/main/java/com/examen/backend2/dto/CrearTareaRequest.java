package com.examen.backend2.dto;
import com.examen.backend2.models.enums.EstadoTarea;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearTareaRequest(
        @NotBlank
        @Size(max = 100)
        String titulo,

        EstadoTarea estado,

        @NotNull
        Long proyectoId
) {}