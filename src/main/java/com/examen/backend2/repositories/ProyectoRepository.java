package com.examen.backend2.repositories;

import com.examen.backend2.models.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByUsuario_Username(String username);

    long countByUsuario_Id(Long usuarioId);
}