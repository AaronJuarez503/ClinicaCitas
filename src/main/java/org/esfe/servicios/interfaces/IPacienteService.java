// IPacienteService.java
package org.esfe.servicios.interfaces;

import java.util.List;
import java.util.Optional;

import org.esfe.modelos.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPacienteService {
    List<Paciente> obtenerTodas(); // Método necesario para CitaController
    Optional<Paciente> buscarPorId(Integer id);
    Paciente crearOCambiar(Paciente paciente);
    void eliminarPorId(Integer id);
    Page<Paciente> buscarTodasPaginadas(Pageable pageable); // Opción de paginación
}
