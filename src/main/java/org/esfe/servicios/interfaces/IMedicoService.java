// IMedicoService.java
package org.esfe.servicios.interfaces;

import java.util.List;
import java.util.Optional;

import org.esfe.modelos.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMedicoService {
    
    List<Medico> obtenerTodas(); // Método necesario para CitaController
    Optional<Medico> buscarPorId(Integer id);
    Medico crearOCambiar(Medico medico);
    void eliminarPorId(Integer id);
    Page<Medico> buscarTodasPaginadas(Pageable pageable); // Opción de paginación
}
