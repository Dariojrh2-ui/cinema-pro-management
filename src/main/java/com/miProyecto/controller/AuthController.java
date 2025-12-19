package com.miProyecto.controller;

import com.miProyecto.model.Usuario;
import com.miProyecto.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // --- VISTAS (GET) ---

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // --- MÉTODOS DE AUTENTICACIÓN (POST) ---

    @PostMapping("/processLogin")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(username);

        if (optionalUsuario.isPresent() && optionalUsuario.get().getPassword().equals(password)) {
            Usuario usuario = optionalUsuario.get();

            // Guardamos datos vitales en la sesión para que Layout Maestro los vea
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("esAdmin", usuario.getEsAdmin());

            if (usuario.getEsAdmin()) {
                return "redirect:/admin";
            } else {
                return "redirect:/welcome";
            }
        } else {
            // Si falla, enviamos mensaje de error y volvemos al login
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario o contraseña incorrectos");
            return "redirect:/login";
        }
    }

    // --- PROCESO DE REGISTRO (POST) ---

    @PostMapping("/processRegister")
    public String processRegistration(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {

        if (usuarioRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ese nombre de usuario ya está en uso.");
            return "redirect:/register";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setEsAdmin(false); // Por defecto no es admin

        usuarioRepository.save(nuevoUsuario);

        redirectAttributes.addFlashAttribute("mensajeExito", "¡Cuenta creada! Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    // --- LOGOUT ---

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalida la sesión completa y elimina todos los atributos
        session.invalidate();
        return "redirect:/login?logout";
    }
}