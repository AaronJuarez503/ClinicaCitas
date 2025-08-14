package org.esfe.repositorios;

import org.esfe.modelos.Usuario;
import org.esfe.modelos.Usuario.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por nombre de usuario exacto (login)
    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    Optional<Usuario> buscarPorNombreUsuario(@Param("nombreUsuario") String nombreUsuario);

    // Buscar usuarios por rol (medico o paciente)
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    List<Usuario> buscarPorRol(@Param("rol") Rol rol);

    // Obtener solo los nombres de usuario de todos los usuarios
    @Query("SELECT u.nombreUsuario FROM Usuario u")
    List<String> obtenerNombresDeUsuario();
}
