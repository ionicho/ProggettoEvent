package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final EventService eventoService;

    public EventController(EventService eventoService) {
        this.eventoService = eventoService;
    }

    // Metodo GET per ottenere un singolo evento
    @GetMapping("/eventi/{id}")
    public Event getEvento(@PathVariable String id) {
        return eventoService.getEvento(id);
    }

    // Metodo GET per ottenere tutti gli eventi
    @GetMapping("/eventi")
    public List<Event> getEventi() {
        return eventoService.getEventi();
    }

    // Metodo POST per aggiungere un evento
    @PostMapping("/eventi/nuovo")
    public Event addEvento(@RequestBody Event evento) {
    	
        eventoService.addEvento(evento);
        return evento;
    }

    // Metodo PUT per aggiornare un evento
    @PutMapping("/eventi/{id}")
    public String updateEvento(@PathVariable String id, @RequestBody Event evento) {
        eventoService.updateEvento(evento);
        return "Evento con ID: " + evento.getId() + " aggiornato con successo.";
    }

    // Metodo DELETE per rimuovere un evento
    @DeleteMapping("/eventi/{id}")
    public String deleteEvento(@PathVariable String id) {
        eventoService.deleteEvento(id);
        return "Evento con ID: " + id + " rimosso con successo.";
    }
}