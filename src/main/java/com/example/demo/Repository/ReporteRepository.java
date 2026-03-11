package com.example.demo.Repository;

import com.example.demo.Model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Integer> {

    List<Reporte> findAllByOrderByFechaEnvioDesc();

    @Modifying
    @Query("DELETE FROM Reporte r WHERE r.usuario.idUsuario = :idUsuario")
    void deleteByUsuario_IdUsuario(@Param("idUsuario") Integer idUsuario);
}