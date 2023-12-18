package PAC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import PAC.model.Evento;
import PAC.service.EventoService;

import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {

    private final EventoService eventoService;

    @Autowired
    public Controller(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    // Metodo GET per ottenere un singolo evento
    @GetMapping("/eventi/{id}")
    public Evento getEvento(@PathVariable String id) {
        return eventoService.getEvento(id);
    }

    // Metodo GET per ottenere tutti gli eventi
    @GetMapping("/eventi")
    public List<Evento> getEventi() {
        return eventoService.getEventi();
    }

    // Metodo POST per aggiungere un evento
    @PostMapping("/eventi")
    public String addEvento(@RequestBody Evento evento) {
        eventoService.addEvento(evento);
        return "Evento aggiunto con successo.";
    }

    // Metodo PUT per aggiornare un evento
    @PutMapping("/eventi/{id}")
    public String updateEvento(@PathVariable String id, @RequestBody Evento evento) {
        eventoService.updateEvento(id, evento);
        return "Evento con ID: " + id + " aggiornato con successo.";
    }

    // Metodo DELETE per rimuovere un evento
    @DeleteMapping("/eventi/{id}")
    public String deleteEvento(@PathVariable String id) {
        eventoService.deleteEvento(id);
        return "Evento con ID: " + id + " rimosso con successo.";
    }
}
