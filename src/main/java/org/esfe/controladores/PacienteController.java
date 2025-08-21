package org.esfe.controladores;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.esfe.modelos.Paciente;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/paciente") // Mapea todas las solicitudes para /paciente
public class PacienteController {

    @Autowired
    private IPacienteService pacienteService; // Inyección del servicio de paciente

    // Muestra el listado paginado de pacientes
    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; // Página actual (0-indexed para Spring Data JPA)
        int pageSize = size.orElse(10); // Tamaño de la página

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Paciente> pacientes = pacienteService.buscarTodasPaginadas(pageable); // Obtener pacientes paginados

        model.addAttribute("pacientes", pacientes); // Añadir la página de pacientes al modelo

        int totalPages = pacientes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers); // Añadir números de página al modelo
        }

        return "Paciente/index"; // <-- ¡CORREGIDO: Apunta a la carpeta "Paciente" (singular, mayúscula)!
    }

    // Muestra el formulario para crear un nuevo paciente
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("paciente", new Paciente()); // Prepara un objeto Paciente vacío para el formulario
        return "Paciente/create"; // <-- ¡CORREGIDO: Apunta a la carpeta "Paciente" (singular, mayúscula)!
    }

    // Guarda un nuevo paciente o actualiza uno existente
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("paciente") Paciente paciente, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            // Si hay errores de validación, regresa al formulario con los errores
            return "Paciente/create"; // <-- ¡CORREGIDO: Apunta a la carpeta "Paciente" (singular, mayúscula)!
        }
        try {
            pacienteService.crearOCambiar(paciente); // Guarda o actualiza el paciente
            attributes.addFlashAttribute("msg", "Paciente guardado exitosamente!"); // Mensaje de éxito
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Hubo un error al guardar el paciente: " + e.getMessage()); // Mensaje de error
        }
        return "redirect:/paciente"; // Redirige al listado de pacientes
    }

    // Muestra los detalles de un paciente específico
    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(id);
        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
        } else {
            attributes.addFlashAttribute("error", "Paciente no encontrado.");
            return "redirect:/paciente";
        }
        return "Paciente/details"; // <-- ¡CORREGIDO: Apunta a la carpeta "Paciente" (singular, mayúscula)!
    }

    // Muestra el formulario para editar un paciente existente
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model, RedirectAttributes attributes) {
        Optional<Paciente> paciente = pacienteService.buscarPorId(id);
        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
        } else {
            attributes.addFlashAttribute("error", "Paciente no encontrado.");
            return "redirect:/paciente";
        }
        return "Paciente/edit"; // <-- ¡CORREGIDO: Apunta a la carpeta "Paciente" (singular, mayúscula)!
    }

    // Elimina un paciente
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            pacienteService.eliminarPorId(id); // Elimina el paciente
            attributes.addFlashAttribute("msg", "Paciente eliminado exitosamente!"); // Mensaje de éxito
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar el paciente: " + e.getMessage()); // Mensaje de error
        }
        return "redirect:/paciente"; // Redirige al listado de pacientes
    }
}
