// Paciente.java
package org.esfe.modelos;

import java.time.LocalDate; // Importar LocalDate

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // Importar EnumType
import jakarta.persistence.Enumerated; // Importar Enumerated
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email; // Importar Email para validación
import jakarta.validation.constraints.NotBlank; // Importar NotBlank para validación

@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del paciente es requerido")
    private String nombre;

    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    private String telefono;

    @Email(message = "El correo electrónico es requerido")
    private String email;

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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Enum para el campo genero
    public enum Genero {
        Masculino,
        Femenino,
        Otro
    }
}