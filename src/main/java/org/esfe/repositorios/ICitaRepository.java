package org.esfe.repositorios;

import org.esfe.modelos.Cita;
import org.esfe.modelos.Cita.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ICitaRepository extends JpaRepository<Cita, Integer> {

    // Buscar citas por ID del médico
    @Query("SELECT c FROM Cita c WHERE c.medico.id = :idMedico")
    List<Cita> buscarPorIdMedico(@Param("idMedico") Integer idMedico);

    // Buscar citas por ID del paciente
    @Query("SELECT c FROM Cita c WHERE c.paciente.id = :idPaciente")
    List<Cita> buscarPorIdPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar citas por estado
    @Query("SELECT c FROM Cita c WHERE c.estado = :estado")
    List<Cita> buscarPorEstado(@Param("estado") Estado estado);

    // Buscar citas en una fecha específica
    @Query("SELECT c FROM Cita c WHERE c.fecha = :fecha")
    List<Cita> buscarPorFecha(@Param("fecha") LocalDateTime fecha);
    
}
