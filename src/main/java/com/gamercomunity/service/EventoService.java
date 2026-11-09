package com.gamercomunity.service;

import com.gamercomunity.models.Evento;
import com.gamercomunity.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> listarProximos() {
        return eventoRepository.findAllByOrderByFechaHoraAsc();
    }

    public List<Evento> buscar(String categoria, LocalDateTime fecha, String ubicacion) {
        return eventoRepository.buscarEventos(categoria, fecha, ubicacion);
    }

    public Evento obtenerPorId(Long id) {
        return eventoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }

    public Evento guardar(Evento evento) {
        return eventoRepository.save(evento);
    }

    public void eliminar(Long id) {
        eventoRepository.deleteById(id);
    }
}