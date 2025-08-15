package org.esfe.controladores;

import org.esfe.modelos.Clinica;
import org.esfe.servicios.interfaces.IClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/clinica")
public class ClinicaController {
    @Autowired
    private IClinicaService clinicaService;

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
}
