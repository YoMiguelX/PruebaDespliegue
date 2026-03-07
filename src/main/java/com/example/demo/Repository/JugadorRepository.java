package com.example.demo.Repository;

import com.example.demo.Model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Integer> {
}
