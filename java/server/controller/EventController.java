package server.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import server.model.Event;
import server.service.EventService;

/**
 * La classe è un componente Spring ed è il controller che
 * risponde alle richieste REST relative alle operazioni CRUD
 * sui {@link server.model.Event}. Sono presenti i metodi:
 * <ul>
    * <li>GET per ottenere tutti gli eventi</li>
    * <li>GET per ottenere un evento</li>
    * <li>GET per ottenere un evento per data e sala</li>
    * <li>GET per aggiungere un evento</li>
    * <li>PUT per aggiornare un evento</li>
    * <li>DELETE per rimuovere un evento</li>
* </ul>
 */

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/event/{date}/{hallName}")
    public Event getEventoByDateHall(@PathVariable String date, @PathVariable String hallName) {
        Event evento = eventService.getEventoByDateHall(LocalDate.parse(date), hallName);
        if (evento == null) {
            throw new SystemException("Non ho trovato l'evento per la data " + date + " e la sala " + hallName);
        }
        return evento;
    }  
    
    // Metodo GET per ottenere un singolo evento
    @GetMapping("/event/{nome}")
    public Event getEvento(@PathVariable String nome) {   
       return eventService.getRisorsa(nome);
    }
    
    // Metodo GET per ottenere tutti gli eventi
    @GetMapping("/event")
    public List<Event> getEventi() {
        return eventService.getRisorse();
    }

    // Metodo GET per invocare il singleton per poter aggiungere un evento
    @GetMapping("/event/add")
    public Event addEvento() {
    	return eventService.addEvento();
    }

    // Metodo PUT per aggiornare un evento   
    @PutMapping("/event/{nome}/update")
    public Event updateEvento(@RequestBody Event evento) {
        System.out.printf("\nEVENT Controller: ricevuto richiesta di update %s\n",evento.toString());
        Event updatedEvent = eventService.updateRisorsa(evento);
        System.out.printf("\nEVENT Controller: inviato risposta ad update %s\n",updatedEvent.toString());
        return updatedEvent;

        //return eventService.updateRisorsa(evento);
    }

    // Metodo DELETE per rimuovere un evento
    @DeleteMapping("/event/{nome}/delete")
    public String deleteEvento(@PathVariable String nome) {
        eventService.deleteRisorsa(nome);
        return "Evento con ID: " + nome + " rimosso con successo.";
    }
}