package com.examen.backend2.controllers;

import com.examen.backend2.dto.CrearProyectoRequest;
import com.examen.backend2.models.Proyecto;
import com.examen.backend2.services.ProyectoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public List<Proyecto> obtenerProyectos(Authentication authentication) {
        return proyectoService.obtenerProyectos(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<Proyecto> crearProyecto(
            Authentication authentication,
            @Valid @RequestBody CrearProyectoRequest request) {

        Proyecto proyecto = proyectoService.crearProyecto(
                authentication.getName(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(proyecto);
    }
}