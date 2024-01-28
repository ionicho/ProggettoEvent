package server.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.*;

import server.model.*;
import server.service.*;

@RestController
@RequestMapping("/api")
public class CalendarController {
	
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // Metodo GET per ottenere il calendario
    @GetMapping("/calendar")
    public Calendar getCalendario() {
        return calendarService.getCalendario();
    }
    
 // Metodo PUT per aggiornare lo stato di una data specifica nel calendario
    @PutMapping("/calendar/state")
    public String setStatoData(@RequestBody StateDate stateDate) {
        calendarService.getCalendario().setStatoData(stateDate.getData(), stateDate.getStato());
        calendarService.salvaCalendarioSuDB();
        return "Stato della data aggiornato con successo e calendario salvato.";
    }

    // Metodo PUT per salvare il calendario modificato
    @PutMapping("/calendar")
    public String salvaCalendario() {
        calendarService.salvaCalendarioSuDB();
        return "Calendario salvato con successo.";
    }

    // Metodo PUT per estendere il calendario
    @PutMapping("/calendar/{startDate}/{endDate}")
    public String setCalendario(@PathVariable String startDate, @PathVariable String endDate) {   	
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        calendarService.setCalendario(start,end);
        return "Calendario esteso con successo e salvato.";
    }
}
