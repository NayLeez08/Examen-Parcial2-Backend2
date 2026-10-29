package com.examen.backend2.controllers;

import com.examen.backend2.models.Usuario;
import com.examen.backend2.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId() != null) {
            return ResponseEntity.badRequest()
                    .body("No envíes el id al crear un usuario.");
        }

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("El username ya está registrado.");
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario datos) {

        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setUsername(datos.getUsername());

                    if (datos.getPassword() != null
                            && !datos.getPassword().isBlank()) {
                        usuario.setPassword(datos.getPassword());
                    }

                    usuario.setRol(datos.getRol());

                    return ResponseEntity.ok(
                            usuarioRepository.save(usuario));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

