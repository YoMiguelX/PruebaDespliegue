package com.example.demo.Repository;

import com.example.demo.Model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {

    /**
     * Obtener preguntas aleatorias por nivel
     * MEJORADO: Más eficiente y compatible con múltiples bases de datos
     */
    @Query(value = "SELECT * FROM preguntas WHERE NIVELES_ID_NIVELES = :nivelId ORDER BY RAND() LIMIT :cantidad",
            nativeQuery = true)
    List<Pregunta> findRandomByNivelId(@Param("nivelId") Integer nivelId, @Param("cantidad") int cantidad);

    /**
     * Contar preguntas por nivel
     */
    @Query("SELECT COUNT(p) FROM Pregunta p WHERE p.nivel.idNiveles = :nivelId")
    long countByNivelId(@Param("nivelId") Integer nivelId);

    /**
     * Obtener todas las preguntas de un nivel (sin aleatorizar)
     */
    @Query("SELECT p FROM Pregunta p WHERE p.nivel.idNiveles = :nivelId")
    List<Pregunta> findByNivelId(@Param("nivelId") Integer nivelId);

    /**
     * Obtener preguntas por mundo (a través del nivel)
     */
    @Query("SELECT p FROM Pregunta p WHERE p.nivel.mundo.id = :mundoId")
    List<Pregunta> findByMundoId(@Param("mundoId") Long mundoId);

    /**
     * Obtener preguntas aleatorias por mundo
     */
    @Query(value = "SELECT p.* FROM preguntas p " +
            "INNER JOIN niveles n ON p.NIVELES_ID_NIVELES = n.idNiveles " +
            "WHERE n.mundos_ID_MUNDOS = :mundoId " +
            "ORDER BY RAND() LIMIT :cantidad",
            nativeQuery = true)
    List<Pregunta> findRandomByMundoId(@Param("mundoId") Long mundoId, @Param("cantidad") int cantidad);

    /**
     * Contar preguntas por mundo
     */
    @Query("SELECT COUNT(p) FROM Pregunta p WHERE p.nivel.mundo.id = :mundoId")
    long countByMundoId(@Param("mundoId") Long mundoId);

    /**
     * Buscar preguntas por texto (para búsqueda en admin)
     */
    @Query("SELECT p FROM Pregunta p WHERE LOWER(p.textoPregunta) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Pregunta> buscarPorTexto(@Param("texto") String texto);

    /**
     * Obtener preguntas por rango de puntos
     */
    @Query("SELECT p FROM Pregunta p WHERE p.puntos >= :min AND p.puntos <= :max")
    List<Pregunta> findByRangoPuntos(@Param("min") Integer min, @Param("max") Integer max);

    /**
     * Verificar si un nivel tiene suficientes preguntas
     */
    @Query("SELECT CASE WHEN COUNT(p) >= :minimo THEN true ELSE false END " +
            "FROM Pregunta p WHERE p.nivel.idNiveles = :nivelId")
    boolean tieneSuficientesPreguntas(@Param("nivelId") Integer nivelId, @Param("minimo") int minimo);
}