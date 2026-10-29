package com.examen.backend2.services;
import com.examen.backend2.models.enums.EstadoTarea;
import com.examen.backend2.dto.ActualizarTareaRequest;
import com.examen.backend2.dto.CrearTareaRequest;
import com.examen.backend2.models.Proyecto;
import com.examen.backend2.models.Tarea;
import com.examen.backend2.repositories.ProyectoRepository;
import com.examen.backend2.repositories.TareaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;

    public TareaService(
            TareaRepository tareaRepository,
            ProyectoRepository proyectoRepository) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Transactional
    public Tarea crearTarea(
            String username, CrearTareaRequest request) {

        Proyecto proyecto = proyectoRepository
                .findById(request.proyectoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Proyecto no encontrado."));

        if (!proyecto.getUsuario().getUsername().equals(username)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes agregar tareas a un proyecto ajeno.");
        }

        Tarea tarea = new Tarea();
        tarea.setTitulo(request.titulo());
        tarea.setEstado(request.estado() == null
                ? EstadoTarea.PENDIENTE : request.estado());
        tarea.setProyecto(proyecto);

        return tareaRepository.save(tarea);
    }

    @Transactional
    public Tarea actualizarEstado(
            String username,
            Long id,
            ActualizarTareaRequest request) {

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tarea no encontrada."));

        if (!tarea.getProyecto().getUsuario()
                .getUsername().equals(username)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No puedes modificar una tarea ajena.");
        }

        tarea.setEstado(request.estado());
        return tareaRepository.save(tarea);
    }
}
