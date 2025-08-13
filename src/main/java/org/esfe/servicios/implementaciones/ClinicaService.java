// package org.esfe.servicios.implementaciones;

// import org.esfe.modelos.clinica;
// import org.esfe.repositorios.IClinicaRepository;
// import org.esfe.servicios.interfaces.IClinicaService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class ClinicaService implements IClinicaService {
//     @Autowired
//     private IClinicaRepository clinicaRepository;

//     public ClinicaService(IClinicaRepository clinicaRepository) {
//         this.clinicaRepository = clinicaRepository;
//     }

//     @Override
//     public Page<clinica> buscarTodosPaginados(Pageable pageable) {
//         return clinicaRepository.findAll(pageable);
//     }

//     @Override
//     public List<clinica> obtenerTodos() {
//         return clinicaRepository.findAll();
//     }

//     @Override
//     public Optional<clinica> buscarPorId(Integer id) {
//         return clinicaRepository.findById(id);
//     }

//     @Override
//     public clinica crearOEditar(clinica clinica) {
//         return clinicaRepository.save(clinica);
//     }

//     @Override
//     public void eliminarPorId(Integer id) {
//         clinicaRepository.deleteById(id);
//     }

// }
