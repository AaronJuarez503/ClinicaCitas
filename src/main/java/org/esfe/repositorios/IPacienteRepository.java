package org.esfe.repositorios;

import org.esfe.modelos.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IPacienteRepository extends JpaRepository<Paciente, Integer> {

    // Buscar pacientes cuyo nombre contenga texto (ignorando mayúsculas)
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Paciente> buscarPorNombre(@Param("nombre") String nombre);

    // Buscar pacientes por fecha exacta de nacimiento
    @Query("SELECT p FROM Paciente p WHERE p.fechaNacimiento = :fecha")
    List<Paciente> buscarPorFechaNacimiento(@Param("fecha") LocalDate fecha);

    // Buscar paciente por email exacto
    @Query("SELECT p FROM Paciente p WHERE p.email = :email")
    Optional<Paciente> buscarPorEmail(@Param("email") String email);

    // Buscar paciente por teléfono exacto
    @Query("SELECT p FROM Paciente p WHERE p.telefono = :telefono")
    Optional<Paciente> buscarPorTelefono(@Param("telefono") String telefono);

    // Obtener solo los nombres de todos los pacientes
    @Query("SELECT p.nombre FROM Paciente p")
    List<String> obtenerNombresPacientes();
}
