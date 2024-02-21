package server.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import server.model.Event;
import server.service.EventService;

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
            throw new SystemException("Non ho trovato l'evento " + id);
        }
        System.out.printf("RISPOSTA INVIATA %s\n",evento.toString());
        return evento;
    }

    @GetMapping("/eventi/{date}/{hallName}")
    public Event getEventoByDateHall(@PathVariable String date, @PathVariable String hallName) {
        Event evento = eventoService.getEventoByDateHall(LocalDate.parse(date), hallName);
        if (evento == null) {
            throw new SystemException("Non ho trovato l'evento per la data " + date + " e la sala " + hallName);
        }
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
        return eventoService.updateEvento(evento);
    }

    // Metodo DELETE per rimuovere un evento
    @DeleteMapping("/eventi/{id}")
    public String deleteEvento(@PathVariable String id) {
        eventoService.deleteEvento(id);
        return "Evento con ID: " + id + " rimosso con successo.";
    }
}