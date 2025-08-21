package org.esfe.servicios.implementaciones;

import java.util.List;
import java.util.Optional;

import org.esfe.modelos.Paciente;
import org.esfe.repositorios.IPacienteRepository;
import org.esfe.servicios.interfaces.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PacienteServiceImpl implements IPacienteService {

    @Autowired
    private IPacienteRepository pacienteRepository;

    // Constructor opcional para inyección de dependencias si no se usa @Autowired en el campo
    // public PacienteServiceImpl(IPacienteRepository pacienteRepository) {
    //     this.pacienteRepository = pacienteRepository;
    // }

    @Override
    public List<Paciente> obtenerTodas() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Paciente crearOCambiar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    public void eliminarPorId(Integer id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    public Page<Paciente> buscarTodasPaginadas(Pageable pageable) {
        return pacienteRepository.findAll(pageable);
    }
}
