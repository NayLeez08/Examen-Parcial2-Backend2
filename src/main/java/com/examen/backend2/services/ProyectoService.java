package com.examen.backend2.services;

import com.examen.backend2.dto.CrearProyectoRequest;
import com.examen.backend2.models.Proyecto;
import com.examen.backend2.models.Usuario;
import com.examen.backend2.repositories.ProyectoRepository;
import com.examen.backend2.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProyectoService(
            ProyectoRepository proyectoRepository,
            UsuarioRepository usuarioRepository) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Proyecto> obtenerProyectos(String username) {
        return proyectoRepository.findByUsuario_Username(username);
    }

    @Transactional
    public Proyecto crearProyecto(
            String username, CrearProyectoRequest request) {

        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario autenticado no encontrado."));

        long cantidad = proyectoRepository.countByUsuario_Id(usuario.getId());

        if (cantidad >= 3) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Has alcanzado el límite de 3 proyectos.");
        }

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(request.nombre());
        proyecto.setDescripcion(request.descripcion());
        proyecto.setUsuario(usuario);

        return proyectoRepository.save(proyecto);
    }
}