package server.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import server.model.*;
import server.service.*;

/**
 * Questa classe intercetta le richieste relative agli EVENTI,
 * gestisce il modello utilizzando le classi del Model;
 * aggiorna il DB utilizzando le classe del Service
 */

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventoService) {
        this.eventService = eventoService;
    }

    // Metodo GET per ottenere un singolo evento
    @GetMapping("/eventi/{id}")
    public Event getEvento(@PathVariable String id) {
        return eventService.getEvento(id);
    }

    // Metodo GET per ottenere tutti gli eventi
    @GetMapping("/eventi")
    public List<Event> getEventi() {
        return eventService.getEventi();
    }

    // Metodo POST per aggiungere un evento
    @PostMapping("/eventi/nuovo")
    public Event addEvento(@RequestBody Event evento) {
        eventService.addEvento(evento);
        return evento;
    }

    // Metodo PUT per aggiornare un evento
    @PutMapping("/eventi/{id}")
    public String updateEvento(@PathVariable String id, @RequestBody Event evento) {
        eventService.updateEvento(evento.getId(), evento);
        return "Evento con ID: " + evento.getId() + " aggiornato con successo.";
    }

    // Metodo DELETE per rimuovere un evento
    @DeleteMapping("/eventi/{id}")
    public String deleteEvento(@PathVariable String id) {
        eventService.deleteEvento(id);
        return "Evento con ID: " + id + " rimosso con successo.";
    }
}