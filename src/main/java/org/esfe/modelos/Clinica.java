package org.esfe.modelos;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType; // Importa para validar URLs
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "clinicas")
public class Clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la clínica es obligatorio.") // Mensaje de validación mejorado
    private String nombre;

    @NotBlank(message = "La dirección de la clínica es obligatoria.") // Mensaje de validación mejorado
    private String direccion;

    @NotBlank(message = "El teléfono de la clínica es obligatorio.") // Mensaje de validación mejorado
    @Pattern(regexp = "[0-9]{4}-[0-9]{4}", message = "El formato del teléfono debe ser 0000-0000.") // Añade patrón para teléfono
    private String telefono;

    @NotBlank(message = "La URL de ubicación es obligatoria.")
    @URL(message = "Debe ser una URL válida (ej. de Google Maps) para la ubicación.") // Añade validación de URL
    private String ubicacionUrl; // Nuevo campo para la URL de Google Maps

    // Constructor vacío (necesario para JPA)
    public Clinica() {
    }

    // Constructor con todos los campos (puedes ajustar este si usas otros constructores)
    public Clinica(String nombre, String direccion, String telefono, String ubicacionUrl) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.ubicacionUrl = ubicacionUrl;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUbicacionUrl() { // Getter para el nuevo campo
        return ubicacionUrl;
    }

    public void setUbicacionUrl(String ubicacionUrl) { // Setter para el nuevo campo
        this.ubicacionUrl = ubicacionUrl;
    }
}
