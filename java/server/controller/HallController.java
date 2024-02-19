package server.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import server.model.*;
import server.service.*;

/**
 * Questa classe intercetta le richieste relative alle SALE,
 * gestisce il modello utilizzando le classi del Model;
 * aggiorna il DB utilizzando le classe del Service
 */

@RestController
@RequestMapping("/api")
public class HallController {
	
    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    // Metodo GET per ottenere una singola sala
    @GetMapping("/hall/{nome}")
    public Hall getHall(@PathVariable String nome) {
        return hallService.getSala(nome);
    }

    // Metodo GET per ottenere tutte le sale
    @GetMapping("/hall")
    public List<Hall> getSale() {
        return hallService.getSale();
    }

    // Metodo POST per aggiungere una sala
    @PostMapping("/hall")
    public String addSala(@RequestBody Hall sala) {
        hallService.addSala(sala);
        return "Sala aggiunta con successo.";
    }

 // Metodo PUT per aggiornare lo stato di una sala e poi salvare l'intero oggetto
    @PutMapping("/hall/{nome}/state")
    public String updateSala(@PathVariable String nome, @RequestBody StateDate statoData) {
    	hallService.updateSala(nome, statoData); 
        return "Stato della sala con nome: " + nome + " aggiornato con successo e sala salvata.";
    }

    // Metodo DELETE per rimuovere una sala
    @DeleteMapping("/hall/{id}/delete")
    public String deleteSala(@PathVariable String nome) {
        hallService.deleteSala(nome);
        return "Camera con nome: " + nome + " rimossa con successo.";
    }   
}