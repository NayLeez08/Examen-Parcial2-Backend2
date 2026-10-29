package com.examen.backend2.services;

import com.examen.backend2.dto.LoginRequest;
import com.examen.backend2.dto.RegistroRequest;
import com.examen.backend2.dto.TokenResponse;
import com.examen.backend2.models.Usuario;
import com.examen.backend2.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Usuario registrar(RegistroRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre de usuario ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(
                passwordEncoder.encode(request.password()));
        usuario.setRol("USER");

        return usuarioRepository.save(usuario);
    }

    public TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository
                .findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Credenciales incorrectas."));

        if (!passwordEncoder.matches(
                request.password(), usuario.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas.");
        }

        String token = jwtService.generarToken(usuario.getUsername());

        return new TokenResponse(
                token, "Bearer", usuario.getUsername());
    }
}