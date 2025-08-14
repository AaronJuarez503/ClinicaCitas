package org.esfe.repositorios;

import org.esfe.modelos.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMedicoRepository extends JpaRepository<Medico, Integer> {

    // Buscar médicos cuyo nombre contenga un texto (ignorando mayúsculas/minúsculas)
    @Query("SELECT m FROM Medico m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Medico> buscarPorNombre(@Param("nombre") String nombre);

    // Buscar médicos por especialidad exacta
    @Query("SELECT m FROM Medico m WHERE m.especialidad = :especialidad")
    List<Medico> buscarPorEspecialidad(@Param("especialidad") String especialidad);

    // Buscar médicos por ID de clínica
    @Query("SELECT m FROM Medico m WHERE m.clinica.id = :idClinica")
    List<Medico> buscarPorIdClinica(@Param("idClinica") Integer idClinica);

    // Buscar por nombre y especialidad exacta
    @Query("SELECT m FROM Medico m WHERE m.nombre = :nombre AND m.especialidad = :especialidad")
    List<Medico> buscarPorNombreYEspecialidad(@Param("nombre") String nombre, @Param("especialidad") String especialidad);

    // Obtener solo los nombres de los médicos
    @Query("SELECT m.nombre FROM Medico m")
    List<String> obtenerNombresMedicos();
}

