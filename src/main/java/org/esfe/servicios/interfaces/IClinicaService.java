package org.esfe.servicios.interfaces;

import org.esfe.modelos.Clinica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IClinicaService {
    Page<Clinica> buscarTodosPaginados(Pageable pageable);

    List<Clinica> obtenerTodos();

    Optional<Clinica> buscarPorId(Integer id);

    Clinica crearOEditar(Clinica clinica);

    void eliminarPorId(Integer id);
}
