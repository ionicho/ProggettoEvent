package server.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import server.model.*;
import server.service.*;

/**
 * La classe è un componente Spring ed è il controller che
 * risponde alle richieste REST relative alle operazioni CRUD
 * sui {@link server.model.Hall}. Sono presenti i metodi:
 * <ul>
    *<li>GET per ottenere tutte le sale</li>
    *<li>GET per ottenere una singola sala</li>
    *<li>GET per ottenere l'elenco delle sale libere in una data specifica</li>
    *<li>GET per aggiungere una sala</li>
    *<li>PUT per aggiornare lo stato di una sala</li>
    *<li>PUT per aggiornare una sala</li>
    *<li>DELETE per rimuovere una sala</li>
* </ul>
*/

@RestController
@RequestMapping("/api")
public class HallController {
	
    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

     // Metodo GET per ottenere l'elenco delle sale libere in una data specifica
    @GetMapping("/hall/libere/{date}")
    public List<String> getSaleLibere(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return hallService.getSaleLibere(localDate);
    }
    
    // Metodo GET per ottenere una singola sala
    @GetMapping("/hall/{nome}")
    public Hall getSala(@PathVariable String nome) {
        return hallService.getRisorsa(nome);
    }

    // Metodo GET per ottenere tutte le sale
    @GetMapping("/hall")
    public List<Hall> getSale() {
        return hallService.getRisorse();
    }

    // Metodo GET per invocare il singleton per aggiungere una sala
    @GetMapping("/hall/add")
    public Hall addSala(@RequestBody Hall sala) {
        return hallService.addRisorsa();
    }

    // Metodo PUT per aggiornare lo stato di una sala e poi salvare l'intero oggetto
    @PutMapping("/hall/{nome}/state")
    public String updateSala(@PathVariable String nome, @RequestBody StateDate statoData) {
    	hallService.changeStatoSala(nome, statoData); 
        return "Stato della sala con nome: " + nome + " aggiornato con successo e sala salvata.";
    }
    
    // Metodo PUT per aggiornare una sala   
    @PutMapping("/hall/{nome}/update") //NOSONAR
    public Hall updateRisorsa(@RequestBody Hall sala) {
        return hallService.updateRisorsa(sala);
    }

    // Metodo PUT per aggiornare le sale
    @PutMapping("/hall/update")
    public List<Hall> updateSale(@RequestBody List<Hall> sale) {
        System.out.printf("%nHALL Controller: ricevuto richiesta di update %n%s%n",sale.toString());
        List<Hall> risposta = hallService.updateRisorse(sale);
        System.out.printf("%nHALL Controller: inviato risposta di update %n%s%n",sale.toString());
        return risposta;
    }

    // Metodo DELETE per rimuovere una sala
    @DeleteMapping("/hall/{nome}/delete")
    public String deleteSala(@PathVariable String nome) {
        hallService.deleteRisorsa(nome);
        return "Camera con nome: " + nome + " rimossa con successo.";
    }   
}