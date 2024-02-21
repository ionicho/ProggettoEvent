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

    // Metodo GET per ottenere il calendario
    @GetMapping("/calendar")
    public server.model.Calendar getCalendario() {
        return calendarService.getCalendario();
    }
    
    // Metodo PUT per aggiornare lo stato di una data specifica nel calendario
    @PutMapping("/calendar/state")
    public String setStatoData(@RequestBody StateDate stateDate) {
        calendarService.getCalendario().setStatoData(stateDate.getData(), stateDate.getStato());
        calendarService.salvaCalendariosuDB();
        return "Stato della data aggiornato con successo e calendario salvato.";
    }

    // Metodo PUT per salvare il calendario modificato
    @PutMapping("/calendar")
    public String salvaCalendario() {
        calendarService.salvaCalendariosuDB();
        return "Calendario salvato con successo.";
    }

    // Metodo PUT per estendere il calendario
    @PutMapping("/calendar/{startDate}/{endDate}")
    public List<String> setCalendario(@PathVariable String startDate, @PathVariable String endDate) {     
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        calendarService.setCalendario(start,end);
        List<StateDate> disponibilita = calendarService.getCalendario().getDisponibilita();
       return notificaAggiornamento(disponibilita);
    }
}