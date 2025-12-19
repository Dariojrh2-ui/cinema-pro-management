package com.miProyecto.controller;

import com.miProyecto.model.Pelicula;
import com.miProyecto.model.Usuario;
import com.miProyecto.repository.PeliculaRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class PublicController {

    private final PeliculaRepository peliculaRepository;

    @Autowired
    public PublicController(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    // --- VISTAS PÚBLICAS ---

    @GetMapping("/")
    public String showUserCatalog(Model model) {
        List<Pelicula> peliculas = peliculaRepository.findAll();
        model.addAttribute("peliculas", peliculas);
        // Redirigimos a welcome, que es nuestra página principal con imagen de fondo
        return "welcome";
    }

    // Nota: El login y register ya están en AuthController,
    // pero mantenerlos aquí como "alias" no hace daño si prefieres centralizar.
    @GetMapping("/welcome")
    public String welcome(Model model, HttpSession session) {
        // 1. Recuperamos el objeto completo de la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        // 2. Verificación de Seguridad: Si la sesión expiró o no existe, al login
        if (usuario == null) {
            return "redirect:/login";
        }

        // 3. Cargamos el catálogo desde la base de datos
        model.addAttribute("peliculas", peliculaRepository.findAll());

        // 4. Datos de sesión para el Layout Maestro (base.html)
        // Es vital que el nombre coincida con lo que pusimos en el HTML: ${esAdmin}
        model.addAttribute("esAdmin", usuario.getEsAdmin());
        model.addAttribute("nombreUsuario", usuario.getNombre());

        return "welcome";
    }

    // --- OPERACIONES DE COMPRA Y ALQUILER ---

    @PostMapping("/comprar-pelicula/{id}")
    @Transactional
    public String comprarPelicula(@PathVariable Long id, Model model, HttpSession session) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de película no válido: " + id));

        if (pelicula.getTotalInventario() != null && pelicula.getTotalInventario() > 0) {
            // 1. Reducir Stock
            pelicula.setTotalInventario(pelicula.getTotalInventario() - 1);

            // 2. Aumentar Unidades Vendidas (para el gráfico)
            int unidadesVendidas = Optional.ofNullable(pelicula.getUnidadesVendidas()).orElse(0);
            pelicula.setUnidadesVendidas(unidadesVendidas + 1);

            peliculaRepository.save(pelicula);

            model.addAttribute("pelicula", pelicula);
            model.addAttribute("esAdmin", session.getAttribute("esAdmin"));
            model.addAttribute("mensajeExito", "¡Compra Confirmada! Has adquirido: " + pelicula.getTitulo());

            return "confirmacionCompra";
        } else {
            model.addAttribute("mensajeError", "Lo sentimos, no hay stock de " + pelicula.getTitulo());
            return "welcome";
        }
    }

    @PostMapping("/alquilar/{id}")
    @Transactional
    public String alquilarPelicula(@PathVariable Long id, Model model, HttpSession session) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de película no válido: " + id));

        if (pelicula.getTotalInventario() != null && pelicula.getTotalInventario() > 0) {
            // 1. Reducir Stock
            pelicula.setTotalInventario(pelicula.getTotalInventario() - 1);

            // 2. OPCIONAL: Podrías llevar un contador separado de "alquileres"
            // o sumarlo a "unidadesVendidas" para que el admin vea que la peli tiene éxito.
            int unidadesVendidas = Optional.ofNullable(pelicula.getUnidadesVendidas()).orElse(0);
            pelicula.setUnidadesVendidas(unidadesVendidas + 1);

            peliculaRepository.save(pelicula);

            model.addAttribute("pelicula", pelicula);
            model.addAttribute("esAdmin", session.getAttribute("esAdmin"));
            model.addAttribute("mensajeExito", "Alquiler de " + pelicula.getTitulo() + " registrado con éxito.");

            return "confirmacionAlquiler";
        } else {
            model.addAttribute("mensajeError", "No hay unidades disponibles para alquilar.");
            return "welcome";
        }
    }
}