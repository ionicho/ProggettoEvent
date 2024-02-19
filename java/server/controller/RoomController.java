package server.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import server.model.*;
import server.service.*;

/**
 * Questa classe intercetta le richieste relative alle CAMERE,
 * gestisce il modello utilizzando le classi del Model;
 * aggiorna il DB utilizzando le classe del Service
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
        return roomService.getCamera(nome);
    }

    // Metodo GET per ottenere tutte le camere
    @GetMapping("/room")
    public List<Room> getCamere() {
        return roomService.getCamere();
    }

    // Metodo POST per aggiungere una camera
    @PostMapping("/room")
    public String addCamera(@RequestBody Room camera) {
        roomService.addCamera(camera);
        return "Camera aggiunta con successo.";
    }

 // Metodo PUT per aggiornare lo stato di una camera e poi salvare l'intero oggetto
    @PutMapping("/room/{nome}/state")
    public String updateCamera(@PathVariable String nome, @RequestBody StateDate statoData) {
        roomService.updateCamera(nome, statoData);      
        return "Stato della camera con nome: " + nome + " aggiornato con successo e camera salvata.";
    }

    // Metodo DELETE per rimuovere una camera
    @DeleteMapping("/room/{id}/delete")
    public String deleteCamera(@PathVariable String nome) {
        roomService.deleteCamera(nome);
        return "Camera con nome: " + nome + " rimossa con successo.";
    }   
}