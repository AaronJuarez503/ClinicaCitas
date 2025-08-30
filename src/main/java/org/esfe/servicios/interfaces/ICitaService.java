package org.esfe.servicios.interfaces;

import org.esfe.modelos.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import org.esfe.excepciones.CitaExistenteException;
import org.esfe.excepciones.HorarioInvalidoException;

public interface ICitaService {

    Page<Cita> buscarTodasPaginadas(Pageable pageable);

    List<Cita> obtenerTodas();

    Optional<Cita> buscarPorId(Integer id);

    Cita crearOCambiar(Cita cita) throws CitaExistenteException, HorarioInvalidoException;

    void eliminarPorId(Integer id);
    
    boolean existeCitaEnFecha(Integer medicoId, Integer pacienteId, Cita cita);
}