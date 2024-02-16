package server.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    public EventController(EventService eventoService) {
        this.eventoService = eventoService;
    }

    // Metodo GET per ottenere tutti gli eventi
    @GetMapping("/eventi")
    public List<Event> getEventi() {
        return eventoService.getEventi();
    }
    
    // Metodo GET per ottenere un singolo evento
    @GetMapping("/eventi/{id}")
    public Event getEvento(@PathVariable String id) {   
        Event evento = eventoService.getEvento(id);
        if (evento == null) {
            throw new ServerException("Non ho trovato l'evento " + id);
        }
        System.out.printf("RISPOSTA INVIATA %s\n",evento.toString());
        return evento;
    }

    // Metodo GET per invocare il singleton per poter aggiungere un evento
    @GetMapping("/eventi/nuovo")
    public Event addEvento() {
    	return eventoService.addEvento();
    }

    // Metodo PUT per aggiornare un evento   
    @PutMapping("/eventi/{id}")
    public Event updateEvento(@RequestBody Event evento) {
        System.out.printf("CONTROLLER prima %s --%d\n", evento.toString(), evento.getCosto());
        return eventoService.updateEvento(evento);
    }

    // Metodo DELETE per rimuovere un evento
    @DeleteMapping("/eventi/{id}")
    public String deleteEvento(@PathVariable String id) {
        eventoService.deleteEvento(id);
        return "Evento con ID: " + id + " rimosso con successo.";
    }
}