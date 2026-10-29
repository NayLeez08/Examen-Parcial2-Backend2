package com.examen.backend2.controllers;

import com.examen.backend2.dto.ActualizarTareaRequest;
import com.examen.backend2.dto.CrearTareaRequest;
import com.examen.backend2.models.Tarea;
import com.examen.backend2.services.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping
    public ResponseEntity<Tarea> crearTarea(
            Authentication authentication,
            @Valid @RequestBody CrearTareaRequest request) {

        Tarea tarea = tareaService.crearTarea(
                authentication.getName(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizarEstado(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ActualizarTareaRequest request) {

        Tarea tarea = tareaService.actualizarEstado(
                authentication.getName(), id, request);

        return ResponseEntity.ok(tarea);
    }
}