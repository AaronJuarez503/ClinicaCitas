package org.esfe.modelos;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_paciente", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_cita_paciente"))
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_medico", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_cita_medico"))
    private Medico medico;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.Programada;

    // Enum para el campo estado
    public enum Estado {
        Programada,    // Estado inicial
        Confirmada,    // Cuando el médico confirma la cita
        Completada,    // Cuando el médico marca la cita como realizada
        Cancelada      // Si se necesita cancelar
    }

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
