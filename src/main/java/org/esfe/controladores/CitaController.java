package org.esfe.controladores;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.esfe.modelos.Cita;
import org.esfe.servicios.interfaces.ICitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cita")
public class CitaController {
    @Autowired
    private ICitaService citaService;

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

        return "cita/index"; // Nombre de la vista (por ejemplo, cita/index.html)
    }
}