package server.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import server.model.*;
import server.service.*;

/**
 * La classe è un componente Spring ed è il controller che
 * risponde alle richieste REST relative alle operazioni CRUD
 * sui {@link server.model.Room}. Sono presenti i metodi:
 * <ul>
    *<li>GET per ottenere una singola camera</li>
    *<li>GET per ottenere tutte le camere</li>
    *<li>GET per aggiungere una camera</li>
    *<li>PUT per aggiornare lo stato di una camera</li>
    *<li>PUT per aggiornare una camera</li>
    *<li>DELETE per rimuovere una camera</li>
* </ul>
*/

@RestController
@RequestMapping("/api")
public class RoomController {
	
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Metodo GET per ottenere una singola camera
    @GetMapping("/room/{nome}")
    public Room getCamera(@PathVariable String nome) {
        return roomService.getRisorsa(nome);
    }

    // Metodo GET per ottenere tutte le camere
    @GetMapping("/room")
    public List<Room> getCamere() {
        return roomService.getRisorse();
    }

    // Metodo GET per invocare il singleton per aggiungere una camera
    @GetMapping("/room/add")
    public Room addRisorsa(@RequestBody Room camera) {
        return roomService.addRisorsa();
    }

    // Metodo PUT per aggiornare lo stato di una camera e poi salvare l'intero oggetto
    @PutMapping("/room/{nome}/state")
    public String updateCamera(@PathVariable String nome, @RequestBody StateDate statoData) {
        roomService.changeStatoCamera(nome, statoData);      
        return "Stato della camera con nome: " + nome + " aggiornato con successo e camera salvata.";
    }
    
    // Metodo PUT per aggiornare una sala   
    @PutMapping("/room/{nome}/update")
    public Room updateRisorsa(@RequestBody Room camera) {
        return roomService.updateRisorsa(camera);
    }

    // Metodo DELETE per rimuovere una camera
    @DeleteMapping("/room/{nome}/delete")
    public String deleteCamera(@PathVariable String nome) {
        roomService.deleteRisorsa(nome);
        return "Camera con nome: " + nome + " rimossa con successo.";
    }   
}