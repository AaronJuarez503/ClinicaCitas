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
    
    // Verificar si existe una cita para el médico en el rango de una hora
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.medico.id = :medicoId " +
           "AND c.fecha BETWEEN :inicio AND :fin")
    boolean existeCitaEnRango(@Param("medicoId") Integer medicoId, 
                             @Param("inicio") LocalDateTime inicio,
                             @Param("fin") LocalDateTime fin);

    // Buscar citas por estado
    @Query("SELECT c FROM Cita c WHERE c.estado = :estado")
    List<Cita> buscarPorEstado(@Param("estado") Estado estado);

    // Buscar citas en una fecha específica
    @Query("SELECT c FROM Cita c WHERE c.fecha = :fecha")
    List<Cita> buscarPorFecha(@Param("fecha") LocalDateTime fecha);
    
    // Verificar si existe una cita para un médico en una fecha y hora específica
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.medico.id = :medicoId AND c.fecha = :fecha AND c.estado != 'Cancelada'")
    boolean existsCitaByMedicoAndFecha(@Param("medicoId") Integer medicoId, @Param("fecha") LocalDateTime fecha);
    
    // Verificar si existe una cita para un paciente en una fecha y hora específica
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.paciente.id = :pacienteId AND c.fecha = :fecha AND c.estado != 'Cancelada'")
    boolean existsCitaByPacienteAndFecha(@Param("pacienteId") Integer pacienteId, @Param("fecha") LocalDateTime fecha);
    
}
