package org.esfe.servicios.interfaces;

import org.esfe.modelos.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICitaService {

    Page<Cita> buscarTodasPaginadas(Pageable pageable);

    List<Cita> obtenerTodas();

    Optional<Cita> buscarPorId(Integer id);

    Cita crearOCambiar(Cita cita);

    void eliminarPorId(Integer id);
}