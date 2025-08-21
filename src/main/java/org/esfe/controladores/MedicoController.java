package org.esfe.controladores;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.esfe.modelos.Medico;
import org.esfe.modelos.Clinica; // Importar el modelo Clinica si se usa en el formulario
import org.esfe.servicios.interfaces.IMedicoService;
import org.esfe.servicios.interfaces.IClinicaService; // Asumiendo que tienes un servicio para Clinica
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/medico") // Mapea todas las solicitudes para /medico
public class MedicoController {

    @Autowired
    private IMedicoService medicoService; // Inyección del servicio de Medico

    // Se asume que IClinicaService existe y tiene un método obtenerTodos()
    // Si IClinicaService o su implementación no existe, esto causará un error
    @Autowired(required = false)
    private IClinicaService clinicaService; // Inyección del servicio de Clinica para el dropdown en el formulario de médico

    // Muestra el listado paginado de médicos
    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Página actual (0-indexed para Spring Data JPA)
        int pageSize = size.orElse(10); // Tamaño de la página

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Medico> medicos = medicoService.buscarTodasPaginadas(pageable); // Obtener médicos paginados

        model.addAttribute("medicos", medicos); // Añadir la página de médicos al modelo

        int totalPages = medicos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers); // Añadir números de página al modelo
        }

        return "Medico/index"; // <-- ¡CORREGIDO: Apunta a la carpeta "Medico" (singular, mayúscula)!
    }

    // Muestra el formulario para crear un nuevo médico
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("medico", new Medico()); // Prepara un objeto Medico vacío para el formulario
        if (clinicaService != null) { // Solo si el servicio de clínica está disponible
            // Asegúrate de que el método para obtener todas las clínicas se llama 'obtenerTodos()' en tu IClinicaService
            List<Clinica> allClinicas = clinicaService.obtenerTodos();
            model.addAttribute("allClinicas", allClinicas); // Carga todas las clínicas para el dropdown
        }
        return "Medico/create"; // <-- ¡CORREGIDO: Apunta a la carpeta "Medico" (singular, mayúscula)!
    }

    // Guarda un nuevo médico o actualiza uno existente
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("medico") Medico medico, BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            // Si hay errores de validación, regresa al formulario con los errores
            if (clinicaService != null) {
                // Asegúrate de que el método para obtener todas las clínicas se llama 'obtenerTodos()' en tu IClinicaService
                List<Clinica> allClinicas = clinicaService.obtenerTodos();
                model.addAttribute("allClinicas", allClinicas); // Vuelve a cargar clínicas si hay errores
            }
            return "Medico/create"; // <-- ¡CORREGIDO: Apunta a la carpeta "Medico" (singular, mayúscula)!
        }
        try {
            medicoService.crearOCambiar(medico); // Guarda o actualiza el médico
            attributes.addFlashAttribute("msg", "Médico guardado exitosamente!"); // Mensaje de éxito
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Hubo un error al guardar el médico: " + e.getMessage()); // Mensaje de error
        }
        return "redirect:/medico"; // Redirige al listado de médicos
    }

    // Muestra los detalles de un médico específico
    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Optional<Medico> medico = medicoService.buscarPorId(id);
        if (medico.isPresent()) {
            model.addAttribute("medico", medico.get());
        } else {
            attributes.addFlashAttribute("error", "Médico no encontrado.");
            return "redirect:/medico";
        }
        return "Medico/details"; // <-- ¡CORREGIDO: Apunta a la carpeta "Medico" (singular, mayúscula)!
    }

    // Muestra el formulario para editar un médico existente
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Optional<Medico> medico = medicoService.buscarPorId(id);
        if (medico.isPresent()) {
            model.addAttribute("medico", medico.get());
            if (clinicaService != null) {
                // Asegúrate de que el método para obtener todas las clínicas se llama 'obtenerTodos()' en tu IClinicaService
                List<Clinica> allClinicas = clinicaService.obtenerTodos();
                model.addAttribute("allClinicas", allClinicas); // Carga todas las clínicas para el dropdown
            }
        } else {
            attributes.addFlashAttribute("error", "Médico no encontrado.");
            return "redirect:/medico";
        }
        return "Medico/edit"; // <-- ¡CORREGIDO: Apunta a la carpeta "Medico" (singular, mayúscula)!
    }

    // Elimina un médico
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            medicoService.eliminarPorId(id); // Elimina el médico
            attributes.addFlashAttribute("msg", "Médico eliminado exitosamente!"); // Mensaje de éxito
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar el médico: " + e.getMessage()); // Mensaje de error
        }
        return "redirect:/medico"; // Redirige al listado de médicos
    }
}
