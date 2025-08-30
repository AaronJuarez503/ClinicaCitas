package org.esfe.controladores;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.esfe.modelos.Cita;
import org.esfe.modelos.Medico;
import org.esfe.modelos.Paciente;
import org.esfe.excepciones.CitaExistenteException;
import org.esfe.excepciones.HorarioInvalidoException;
import org.esfe.servicios.interfaces.ICitaService;
import org.esfe.servicios.interfaces.IMedicoService;
import org.esfe.servicios.interfaces.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cita")
public class CitaController {

    private void cargarDatosFormulario(Model model) {
        List<Paciente> allPacientes = pacienteService.obtenerTodas();
        List<Medico> allMedicos = medicoService.obtenerTodas();
        
        // Obtener lista única de especialidades
        Set<String> especialidades = allMedicos.stream()
                                             .map(Medico::getEspecialidad)
                                             .collect(Collectors.toSet());

        model.addAttribute("allPacientes", allPacientes);
        model.addAttribute("allMedicos", allMedicos);
        model.addAttribute("especialidades", especialidades);
    }
    @Autowired
    private ICitaService citaService;

    @Autowired
    private IPacienteService pacienteService;

    @Autowired
    private IMedicoService medicoService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(10);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Cita> citas = citaService.buscarTodasPaginadas(pageable);

        model.addAttribute("citas", citas);

        int totalPages = citas.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        // CAMBIO AQUI: La vista ahora está en la carpeta "citas"
        return "citas/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("cita", new Cita());
        cargarDatosFormulario(model);
        return "citas/create";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("cita") Cita cita, BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            cargarDatosFormulario(model); // Método auxiliar para cargar todos los datos necesarios
            model.addAttribute("error", "Error al guardar la cita. Verifique los campos.");
            return "citas/create";
        }
        try {
            citaService.crearOCambiar(cita);
            attributes.addFlashAttribute("msg", "Cita guardada exitosamente!");
            return "redirect:/cita";
        } catch (CitaExistenteException | HorarioInvalidoException e) {
            cargarDatosFormulario(model); // Método auxiliar para cargar todos los datos necesarios
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cita", cita); // Mantener los datos ingresados
            return "citas/create";
        } catch (Exception e) {
            cargarDatosFormulario(model); // Método auxiliar para cargar todos los datos necesarios
            model.addAttribute("error", "Hubo un error al guardar la cita: " + e.getMessage());
            model.addAttribute("cita", cita); // Mantener los datos ingresados
            return "citas/create";
        }
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model) {
        // CAMBIO AQUI: La vista ahora está en la carpeta "citas"
        return "citas/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        // CAMBIO AQUI: La vista ahora está en la carpeta "citas"
        return "citas/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            citaService.eliminarPorId(id);
            attributes.addFlashAttribute("msg", "Cita eliminada exitosamente!");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar la cita: " + e.getMessage());
        }
        return "redirect:/cita";
    }

    @GetMapping("/medicos-por-especialidad")
    @ResponseBody
    public List<Medico> getMedicosPorEspecialidad(@RequestParam String especialidad) {
        List<Medico> allMedicos = medicoService.obtenerTodas();
        return allMedicos.stream()
                        .filter(m -> m.getEspecialidad().equals(especialidad))
                        .collect(Collectors.toList());
    }
}