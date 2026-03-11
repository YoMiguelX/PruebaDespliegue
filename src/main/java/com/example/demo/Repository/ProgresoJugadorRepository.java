package com.example.demo.Repository;

import com.example.demo.Model.Jugador;
import com.example.demo.Model.ProgresoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProgresoJugadorRepository extends JpaRepository<ProgresoJugador, Integer> {

    @Modifying
    @Query("DELETE FROM ProgresoJugador p WHERE p.jugador = :jugador")
    void deleteByJugador(@Param("jugador") Jugador jugador);
}