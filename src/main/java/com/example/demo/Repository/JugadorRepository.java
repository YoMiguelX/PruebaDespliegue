package com.example.demo.Repository;

import com.example.demo.Model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JugadorRepository extends JpaRepository<Jugador, Integer> {


    Optional<Jugador> findByUsuario_IdUsuario(Integer idUsuario);

    List<Jugador> findAllByUsuario_IdUsuario(Integer idUsuario);

    boolean existsByNombre(String nombre);

    @Modifying
    @Query("UPDATE Jugador j SET j.progreso = null WHERE j.usuario.idUsuario = :idUsuario")
    void nullifyProgresoByUsuario(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Query("DELETE FROM Jugador j WHERE j.usuario.idUsuario = :idUsuario")
    void deleteByUsuario_IdUsuario(@Param("idUsuario") Integer idUsuario);
}