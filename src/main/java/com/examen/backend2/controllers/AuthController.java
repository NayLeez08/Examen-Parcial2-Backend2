package com.examen.backend2.controllers;

import com.examen.backend2.dto.LoginRequest;
import com.examen.backend2.dto.RegistroRequest;
import com.examen.backend2.dto.TokenResponse;
import com.examen.backend2.models.Usuario;
import com.examen.backend2.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody RegistroRequest request) {
        Usuario usuario = authService.registrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "mensaje", "Usuario registrado correctamente",
                        "id", usuario.getId(),
                        "username", usuario.getUsername(),
                        "rol", usuario.getRol()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}