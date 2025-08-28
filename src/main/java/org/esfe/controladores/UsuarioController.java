package org.esfe.controladores;

import org.esfe.modelos.Rol;
import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IRolService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @GetMapping("/create")
    public String create(Usuario usuario, Model model){
        model.addAttribute("roles", rolService.obtenerTodos());
        return "usuario/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam("rol") Integer rol, Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(usuario);
            model.addAttribute("roles", rolService.obtenerTodos());
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "usuario/create";
        }

        String password = passwordEncoder.encode(usuario.getClave());
        Rol perfil = new Rol();
        perfil.setId(rol);

        usuario.setStatus(1);
        usuario.agregar(perfil);
        usuario.setClave(password);
        usuarioService.crearOEditar(usuario);
        attributes.addFlashAttribute("msg", "Usuario creado correctamente");
        return "redirect:/login";
    }

}