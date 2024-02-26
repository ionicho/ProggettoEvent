package server.controller;

import java.time.LocalDate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import server.model.*;
import server.service.*;
import server.*;

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
            List<String> toReschedule = curr.updateState(disponibilita);
            allToReschedule.addAll(toReschedule);
        }
        return allToReschedule;
    }
    
    // Metodo GET per ottenere tutti calendari
    @GetMapping("/calendar")
    public List<server.model.Calendar> getCalendari() {
        return calendarService.getCalendari();
    }

    // Metodo GET per ottenere il calendario
    @GetMapping("/calendar/{nome}")
    public server.model.Calendar getCalendario(@PathVariable String nome) {
        return calendarService.getCalendario(nome);
    }
    
    // Metodo PUT per aggiornare lo stato di una data specifica nel calendario
    @PutMapping("/calendar/{nome}/state")
    public String updateCalendario(@PathVariable String nome, @RequestBody StateDate statoData) {
        calendarService.updateCalendario(nome, statoData);      
        return "Stato della clendario con nome: " + nome + " aggiornato con successo e clendario salvato";
    }

    // Metodo PUT per estendere il calendario
    @PutMapping("/calendar/{nome}/{startDate}/{endDate}")
    public List<String> setCalendario(@PathVariable String nome, @PathVariable String startDate, @PathVariable String endDate) {     
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        calendarService.setCalendario(nome,start,end);
        List<StateDate> disponibilita = calendarService.getCalendario(nome).getDisponibilita();
       return notificaAggiornamento(disponibilita);
    }
}