package org.esfe.repositorios;

import org.esfe.modelos.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;  

public interface IClinicaRepository extends JpaRepository<Clinica, Integer> {

    // Buscar clínicas cuyo nombre contenga texto (ignora mayúsculas)
    List<Clinica> findByNombreContainingIgnoreCase(String nombre);

    // Buscar una clínica por dirección exacta
    Optional<Clinica> findByDireccion(String direccion);

    // Buscar clínicas por teléfono exacto con JPQL
    @Query("SELECT c FROM Clinica c WHERE c.telefono = :telefono")
    List<Clinica> buscarPorTelefono(@Param("telefono") String telefono);

    // Buscar clínicas por nombre y dirección (ambos exactos)
    List<Clinica> findByNombreAndDireccion(String nombre, String direccion);

    // Obtener solo los nombres de todas las clínicas
    @Query("SELECT c.nombre FROM Clinica c")
    List<String> obtenerNombresClinicas();
}
