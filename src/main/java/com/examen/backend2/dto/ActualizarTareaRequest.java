package com.examen.backend2.dto;
import com.examen.backend2.models.enums.EstadoTarea;
import jakarta.validation.constraints.NotNull;

public record ActualizarTareaRequest(
        @NotNull EstadoTarea estado
) {}