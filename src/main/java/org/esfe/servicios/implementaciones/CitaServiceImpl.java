package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Cita;
import org.esfe.repositorios.ICitaRepository;
import org.esfe.servicios.interfaces.ICitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalTime;
import java.time.LocalDateTime;
import org.esfe.excepciones.CitaExistenteException;
import org.esfe.excepciones.HorarioInvalidoException;

@Service
public class CitaServiceImpl implements ICitaService {

    @Autowired
    private ICitaRepository citaRepository;

    @Override
    public Page<Cita> buscarTodasPaginadas(Pageable pageable) {
        return citaRepository.findAll(pageable);
    }

    @Override
    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> buscarPorId(Integer id) {
        return citaRepository.findById(id);
    }

    @Override
    public Cita crearOCambiar(Cita cita) throws CitaExistenteException, HorarioInvalidoException {
        // Si es una actualización (tiene ID), no validamos duplicados pero sí horario
        if (cita.getId() == null) {
            // Validar si ya existe una cita para el médico o paciente en esa fecha y hora
            if (existeCitaEnFecha(cita.getMedico().getId(), cita.getPaciente().getId(), cita)) {
                throw new CitaExistenteException("Ya existe una cita programada para esa fecha y hora.");
            }
        }
        
        // Validar el horario
        LocalTime horaInicio = LocalTime.of(8, 0); // 8:00 AM
        LocalTime horaFin = LocalTime.of(17, 0);   // 5:00 PM
        
        // Verificar que la fecha no sea nula
        if (cita.getFecha() == null) {
            throw new HorarioInvalidoException("La fecha y hora de la cita no puede estar vacía.");
        }
        
        LocalTime horaCita = cita.getFecha().toLocalTime();

        // Validar que la cita esté dentro del horario permitido
        if (horaCita.isBefore(horaInicio) || horaCita.isAfter(horaFin)) {
            throw new HorarioInvalidoException("Las citas solo se pueden programar entre 8:00 AM y 5:00 PM.");
        }

        // Validar si hay alguna cita en el rango de una hora
        LocalDateTime fechaCita = cita.getFecha();
        LocalDateTime inicioRango = fechaCita.minusMinutes(59);
        LocalDateTime finRango = fechaCita.plusMinutes(59);
        
        if (citaRepository.existeCitaEnRango(cita.getMedico().getId(), inicioRango, finRango)) {
            throw new HorarioInvalidoException(
                "Ya existe una cita programada dentro de la hora anterior o siguiente. " +
                "Cada cita requiere una hora completa de atención."
            );
        }
        
        return citaRepository.save(cita);
    }

    @Override
    public boolean existeCitaEnFecha(Integer medicoId, Integer pacienteId, Cita cita) {
        return citaRepository.existsCitaByMedicoAndFecha(medicoId, cita.getFecha()) ||
               citaRepository.existsCitaByPacienteAndFecha(pacienteId, cita.getFecha());
    }

    @Override
    public void eliminarPorId(Integer id) {
        citaRepository.deleteById(id);
    }
}