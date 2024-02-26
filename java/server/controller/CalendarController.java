package server.controller;

import java.time.LocalDate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import server.model.*;
import server.service.*;
import server.*;

/**
 * La classe è un componente Spring ed è il controller che
 * risponde alle richieste REST relative alle operazioni CRUD
 * sui {@link server.model.Calendar}. Sono presenti i metodi:
 * <ul>
    * <li>GET per ottenere tutti i calendari</li>
    * <li>GET per ottenere un calendario</li>
    * <li>PUT per aggiornare lo stato di una data specifica nel calendario</li>
    * <li>PUT per estendere il calendario</li>
 * </ul>
 * La classe implementa l'interfaccia {@link Subscriber} per ricevere
 * notifiche di cambiamento di disponbilità della struttura.
 */

@RestController
@RequestMapping("/api")
public class CalendarController {
	
    private final CalendarService calendarService;
    private final HallService hallService;
    private final RoomService roomService;
    private List<Subscriber> subscribers;

    public CalendarController(CalendarService calendarService, HallService hallService, RoomService roomService) {
        this.calendarService = calendarService;
        this.hallService = hallService;
        this.roomService = roomService;
        this.subscribers = new ArrayList<>();
        this.subscribers.add(this.hallService);
        this.subscribers.add(this.roomService);
    }

    public List<String> notificaAggiornamento(List<StateDate> disponibilita) {
        List<String> allToReschedule = new ArrayList<>();
        for (Subscriber curr : subscribers) {
            List<String> toReschedule = curr.changeDisponibilita(disponibilita);
            allToReschedule.addAll(toReschedule);
        }
        return allToReschedule;
    }
    
    // Metodo GET per ottenere tutti calendari
    @GetMapping("/calendar")
    public List<server.model.Calendar> getCalendari() {
        return calendarService.getRisorse();
    }

    // Metodo GET per ottenere il calendario
    @GetMapping("/calendar/{nome}")
    public server.model.Calendar getCalendario(@PathVariable String nome) {
        return calendarService.getRisorsa(nome);
    }
    
    // Metodo PUT per aggiornare lo stato di una data specifica nel calendario
    @PutMapping("/calendar/{nome}/state")
    public String updateCalendario(@PathVariable String nome, @RequestBody StateDate statoData) {
        calendarService.changeStatoCalendario(nome, statoData);      
        return "Stato della clendario con nome: " + nome + " aggiornato con successo e clendario salvato";
    }

    // Metodo PUT per estendere il calendario
    @PutMapping("/calendar/{nome}/{startDate}/{endDate}")
    public List<String> setCalendario(@PathVariable String nome, @PathVariable String startDate, @PathVariable String endDate) {     
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        calendarService.setCalendario(nome,start,end);
        List<StateDate> disponibilita = calendarService.getRisorsa(nome).getDisponibilita();
       return notificaAggiornamento(disponibilita);
    }
}