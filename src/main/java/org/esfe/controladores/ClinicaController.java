package org.esfe.controladores;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.esfe.modelos.Clinica;
import org.esfe.servicios.PdfGeneratorService;
import org.esfe.servicios.interfaces.IClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid; // Importante: Asegúrate de que esta importación exista

@Controller
@RequestMapping("/clinica")
public class ClinicaController {
    @Autowired
    private IClinicaService clinicaService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(10);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Clinica> clinicas = clinicaService.buscarTodosPaginados(pageable);
        
        model.addAttribute("clinicas", clinicas);

        int totalPages = clinicas.getTotalPages();
        if ( totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "clinica/index";
    }

    @GetMapping("/create")
    public String crear(Clinica clinica) {
        // Al pasar un objeto Clinica al método, Spring lo añade al modelo automáticamente.
        // Esto es necesario para que Thymeleaf pueda enlazar los campos del formulario.
        return "clinica/create";
    }

    @PostMapping("/save")
    public String save(@Valid Clinica clinica, BindingResult result, Model model, RedirectAttributes attributes){
        // La anotación @Valid es CRÍTICA aquí para que Spring Boot procese las
        // anotaciones de validación (@NotBlank, @URL, @Pattern) definidas en el modelo Clinica.
        if(result.hasErrors()){
            // Si hay errores, no uses model.addAttribute(clinica); porque el objeto clinica
            // ya está en el modelo debido a @ModelAttribute (implicado cuando el objeto
            // se pasa directamente al método con @Valid).
            attributes.addFlashAttribute("error", "No se pudo guardar debido a errores de validación.");
            return "clinica/create";
        }

        try {
            clinicaService.crearOEditar(clinica);
            attributes.addFlashAttribute("msg", "Clínica guardada correctamente.");
            return "redirect:/clinica";
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Hubo un error al guardar la clínica: " + e.getMessage());
            return "clinica/create"; // Vuelve al formulario en caso de error de servicio
        }
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        // Es buena práctica manejar el Optional para evitar NullPointerExceptions
        Optional<Clinica> clinicaOptional = clinicaService.buscarPorId(id);
        if (clinicaOptional.isPresent()) {
            model.addAttribute("clinica", clinicaOptional.get());
            return "clinica/details";
        } else {
            // Si la clínica no se encuentra, redirige con un mensaje de error
            return "redirect:/clinica?error=Clinica no encontrada";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        // Es buena práctica manejar el Optional para evitar NullPointerExceptions
        Optional<Clinica> clinicaOptional = clinicaService.buscarPorId(id);
        if (clinicaOptional.isPresent()) {
            model.addAttribute("clinica", clinicaOptional.get());
            return "clinica/edit";
        } else {
            // Si la clínica no se encuentra, redirige con un mensaje de error
            return "redirect:/clinica?error=Clinica no encontrada para editar";
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        // Es buena práctica manejar el Optional para evitar NullPointerExceptions
        Optional<Clinica> clinicaOptional = clinicaService.buscarPorId(id);
        if (clinicaOptional.isPresent()) {
            model.addAttribute("clinica", clinicaOptional.get());
            return "clinica/delete";
        } else {
            // Si la clínica no se encuentra, redirige con un mensaje de error
            return "redirect:/clinica?error=Clinica no encontrada para eliminar";
        }
    }

    @PostMapping("/delete")
    public String delete(Clinica clinica, RedirectAttributes attributes){
        try {
            clinicaService.eliminarPorId(clinica.getId());
            attributes.addFlashAttribute("msg", "Clínica eliminada correctamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Hubo un error al eliminar la clínica: " + e.getMessage());
        }
        return "redirect:/clinica";
    }

    @GetMapping("/reporte/{visualizacion}")
    public ResponseEntity<byte[]> ReporteGeneral(@PathVariable("visualizacion") String visualizacion) {
        try {
            List<Clinica> clinicas = clinicaService.obtenerTodos(); // Usar la lista de clínicas
            byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml("reporte/reporte", "clinicas", clinicas); // Pasa "clinicas"

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", visualizacion + "; filename=reporte_clinicas.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
