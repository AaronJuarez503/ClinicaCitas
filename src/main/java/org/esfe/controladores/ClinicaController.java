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
import org.springframework.http.HttpStatus;
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

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
    return "clinica/create";
}

@PostMapping("/save")
    public String save(Clinica clinica, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(clinica);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "clinica/create";
        }

        clinicaService.crearOEditar(clinica);
        attributes.addFlashAttribute("msg", "Clinica creada correctamente");
        return "redirect:/clinica";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Clinica clinica = clinicaService.buscarPorId(id).get();
        model.addAttribute("clinica", clinica);
        return "clinica/details";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Clinica clinica = clinicaService.buscarPorId(id).get();
        model.addAttribute("clinica", clinica);
        return "clinica/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Clinica clinica = clinicaService.buscarPorId(id).get();
        model.addAttribute("clinica", clinica);
        return "clinica/delete";
    }

    @PostMapping("/delete")
    public String delete(Clinica clinica, RedirectAttributes attributes){
        clinicaService.eliminarPorId(clinica.getId());
        attributes.addFlashAttribute("msg", "clinica eliminada correctamente");
        return "redirect:/clinica";
    }

    @GetMapping("/reporte/{visualizacion}")
    public ResponseEntity<byte[]> ReporteGeneral(@PathVariable("visualizacion") String visualizacion) {

        try {
            List<Clinica> grupos = clinicaService.obtenerTodos();

            // Genera el PDF. Si hay un error aquí, la excepción será capturada.
            byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml("reporte/reporte", "clinicas", grupos);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);           
            // inline= vista previa, attachment=descarga el archivo
           headers.add("Content-Disposition", visualizacion+"; filename=reporte_general.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}