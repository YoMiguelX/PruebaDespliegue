package com.example.demo.Repository;

import com.example.demo.Model.Jugador;
import com.example.demo.Model.RespuestasJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RespuestasJugadorRepository extends JpaRepository<RespuestasJugador, Integer> {

    @Modifying
    @Query("DELETE FROM RespuestasJugador r WHERE r.jugador = :jugador")
    void deleteByJugador(@Param("jugador") Jugador jugador);
}