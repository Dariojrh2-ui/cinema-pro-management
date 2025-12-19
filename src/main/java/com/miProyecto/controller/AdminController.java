package com.miProyecto.controller;

import com.miProyecto.model.Pelicula;
import com.miProyecto.model.Usuario;
import com.miProyecto.repository.PeliculaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Importación necesaria
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PeliculaRepository peliculaRepository;

    // Inyectamos la tasa desde application.properties
    @Value("${tasa.cambio:60.0}")
    private double tasaCambio;

    @Autowired
    public AdminController(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
    }

    // ---------------------------------------------------
    // 1. VISTAS PRINCIPALES (DASHBOARD DINÁMICO)
    // ---------------------------------------------------

    @GetMapping
    public String showAdminDashboard(Model model, HttpSession session) {
        // Seguridad: Verificar sesión y rol
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !usuario.getEsAdmin()) {
            return "redirect:/login";
        }

        List<Pelicula> todas = peliculaRepository.findAll();

        // --- CÁLCULOS REALES PARA EL ADMINISTRADOR ---

        // 1. Películas Agotadas (Stock <= 0)
        List<Pelicula> agotadas = todas.stream()
                .filter(p -> p.getTotalInventario() != null && p.getTotalInventario() <= 0)
                .collect(Collectors.toList());

        // 2. Película Más Vendida
        Pelicula masVendida = todas.stream()
                .filter(p -> p.getUnidadesVendidas() != null)
                .max(Comparator.comparing(Pelicula::getUnidadesVendidas))
                .orElse(null);

        // 3. Película Menos Vendida
        Pelicula menosVendida = todas.stream()
                .filter(p -> p.getUnidadesVendidas() != null)
                .min(Comparator.comparing(Pelicula::getUnidadesVendidas))
                .orElse(null);

        // 4. Cálculo de Ganancias (Primero en moneda local)
        double gananciasLocales = todas.stream()
                .filter(p -> p.getPrecioCompra() != null && p.getUnidadesVendidas() != null)
                .mapToDouble(p -> p.getUnidadesVendidas() * p.getPrecioCompra().doubleValue())
                .sum();

        // 5. Conversión a Dólares
        double gananciasDolares = gananciasLocales / tasaCambio;

        // --- ENVIAR DATOS AL MODELO ---
        model.addAttribute("esAdmin", true);
        model.addAttribute("peliculas", todas); // Necesario para la vista previa del catálogo
        model.addAttribute("totalAgotadas", agotadas.size());
        model.addAttribute("peliculasAgotadas", agotadas);

        model.addAttribute("peliculaMasVendida", masVendida != null ? masVendida.getTitulo() : "N/A");
        model.addAttribute("unidadesMasVendida", masVendida != null ? masVendida.getUnidadesVendidas() : 0);
        model.addAttribute("peliculaMenosVendida", menosVendida != null ? menosVendida.getTitulo() : "N/A");

        // Enviamos el valor ya convertido a Dólares
        model.addAttribute("gananciasTotales", gananciasDolares);

        // --- PREPARAR DATOS PARA EL GRÁFICO ---
        List<String> listaNombres = todas.stream()
                .map(Pelicula::getTitulo)
                .collect(Collectors.toList());

        List<Integer> listaVentas = todas.stream()
                .map(p -> p.getUnidadesVendidas() != null ? p.getUnidadesVendidas() : 0)
                .collect(Collectors.toList());

        model.addAttribute("nombresGrafico", listaNombres);
        model.addAttribute("datosGrafico", listaVentas);

        return "admin";
    }

    // ... (resto de métodos GetMapping y PostMapping se mantienen igual)

    @GetMapping("/catalog")
    public String showAdminCatalog(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null)
            return "redirect:/login";

        List<Pelicula> peliculas = peliculaRepository.findAll();
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("esAdmin", true);
        return "catalogoAdmin";
    }

    @GetMapping("/inventario")
    public String showPeliculaInventario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null)
            return "redirect:/login";

        List<Pelicula> peliculas = peliculaRepository.findAll();
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("esAdmin", true);
        return "inventarioPeliculas";
    }

    @GetMapping("/agregarPelicula")
    public String mostrarFormularioAgregarPelicula(Model model) {
        Pelicula pelicula = new Pelicula();
        pelicula.setTotalInventario(0);
        pelicula.setUnidadesVendidas(0);

        model.addAttribute("pelicula", pelicula);
        model.addAttribute("esAdmin", true);
        return "agregarPelicula";
    }

    @GetMapping("/pelicula/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de película no válido: " + id));

        model.addAttribute("pelicula", pelicula);
        model.addAttribute("esAdmin", true);
        return "agregarPelicula";
    }

    @PostMapping("/pelicula/save")
    public String savePelicula(@ModelAttribute("pelicula") Pelicula pelicula,
            RedirectAttributes redirectAttributes) {

        if (pelicula.getTotalInventario() == null)
            pelicula.setTotalInventario(0);
        if (pelicula.getUnidadesVendidas() == null)
            pelicula.setUnidadesVendidas(0);

        peliculaRepository.save(pelicula);
        redirectAttributes.addFlashAttribute("mensajeExito", "¡Película guardada correctamente!");

        return "redirect:/admin/catalog";
    }

    @PostMapping("/pelicula/delete/{id}")
    public String deletePelicula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            peliculaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Película eliminada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar: " + e.getMessage());
        }
        return "redirect:/admin/catalog";
    }

    @GetMapping("/errorAdmin")
    public String showErrorAdmin(Model model) {
        model.addAttribute("esAdmin", false);
        return "errorAdmin";
    }
}