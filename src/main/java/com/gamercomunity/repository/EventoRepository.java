package com.gamercomunity.repository;

import com.gamercomunity.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("SELECT e FROM Evento e WHERE " +
           "(:categoria IS NULL OR :categoria = '' OR e.categoria = :categoria) AND " +
           "(:fecha IS NULL OR FUNCTION('DATE', e.fechaHora) = FUNCTION('DATE', :fecha)) AND " +
           "(:ubicacion IS NULL OR :ubicacion = '' OR LOWER(e.direccion) LIKE LOWER(CONCAT('%', :ubicacion, '%'))) " +
           "ORDER BY e.fechaHora ASC")
    List<Evento> buscarEventos(@Param("categoria") String categoria,
                               @Param("fecha") LocalDateTime fecha,
                               @Param("ubicacion") String ubicacion);

    List<Evento> findAllByOrderByFechaHoraAsc();
}