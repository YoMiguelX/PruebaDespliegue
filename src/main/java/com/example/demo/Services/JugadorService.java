package com.example.demo.Services;

import com.example.demo.Model.Jugador;
import com.example.demo.Repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    public Page<Jugador> buscarConFiltros(String nombreJugador, String nombreUsuario, String mundo, String estado, Pageable pageable) {
        // Por ahora devolvemos todos, luego puedes implementar filtros con @Query
        return jugadorRepository.findAll(pageable);
    }

    public long contarTotal() {
        return jugadorRepository.count();
    }

    public long contarActivos() {
        return jugadorRepository.findAll().stream()
                .filter(j -> "ACTIVO".equalsIgnoreCase(j.getEstado()))
                .count();
    }

    public double calcularProgresoPromedio() {
        return jugadorRepository.findAll().stream()
                .mapToDouble(j -> j.getProgreso() != null ? j.getProgreso().getPuntajeNivel() : 0)
                .average()
                .orElse(0);
    }

}
