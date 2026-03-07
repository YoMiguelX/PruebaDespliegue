package com.example.demo.Controller;

import com.example.demo.Model.Jugador;
import com.example.demo.Model.Mundo;
import com.example.demo.Services.JugadorService;
import com.example.demo.Services.MundoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class JugadorController {

    private final JugadorService jugadorService;
    private final MundoService mundoService;

    @Autowired
    public JugadorController(JugadorService jugadorService, MundoService mundoService) {
        this.jugadorService = jugadorService;
        this.mundoService = mundoService;
    }

    @GetMapping("/jugador")
    public String estadoJugadores(
            @RequestParam(required = false) String nombreJugador,
            @RequestParam(required = false) String nombreUsuario,
            @RequestParam(required = false) String mundo,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Jugador> jugadores = jugadorService.buscarConFiltros(
                nombreJugador,
                nombreUsuario,
                mundo,
                estado,
                pageable
        );

        List<Mundo> mundos = mundoService.findAll();

        model.addAttribute("jugadores", jugadores);
        model.addAttribute("mundos", mundos);
        model.addAttribute("page", jugadores);
        model.addAttribute("totalJugadores", jugadorService.contarTotal());
        model.addAttribute("jugadoresActivos", jugadorService.contarActivos());
        model.addAttribute("progresoPromedio", jugadorService.calcularProgresoPromedio());

        return "jugador"; // tu vista Thymeleaf
    }
}
