package org.esfe.servicios.implementaciones;

import java.util.List;
import java.util.Optional;

import org.esfe.modelos.Medico;
import org.esfe.repositorios.IMedicoRepository;
import org.esfe.servicios.interfaces.IMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements IMedicoService {

    @Autowired
    private IMedicoRepository medicoRepository;

    // Constructor opcional para inyección de dependencias si no se usa @Autowired en el campo
    // public MedicoServiceImpl(IMedicoRepository medicoRepository) {
    //     this.medicoRepository = medicoRepository;
    // }

    @Override
    public List<Medico> obtenerTodas() {
        return medicoRepository.findAll();
    }

    @Override
    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    @Override
    public Medico crearOCambiar(Medico medico) {
        return medicoRepository.save(medico);
    }

    @Override
    public void eliminarPorId(Integer id) {
        medicoRepository.deleteById(id);
    }

    @Override
    public Page<Medico> buscarTodasPaginadas(Pageable pageable) {
        return medicoRepository.findAll(pageable);
    }
}
