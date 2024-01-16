package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import server.model.*;
import server.service.*;

import java.util.*;

/**
 * Questa classe intercetta le richieste relative alle camere
 */

@RestController
@RequestMapping("/api")
public class RoomController {
	
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Metodo GET per ottenere una singola camera
    @GetMapping("/room/{nome}")
    public ResourceRoom getCamera(@PathVariable String nome) {
        return roomService.getCamera(nome);
    }

    // Metodo GET per ottenere tutte le camere
    @GetMapping("/room")
    public List<ResourceRoom> getCamere() {
        return roomService.getCamere();
    }

    // Metodo POST per aggiungere una camera
    @PostMapping("/room")
    public String addCamera(@RequestBody ResourceRoom camera) {
        roomService.addCamera(camera);
        return "Evento aggiunto con successo.";
    }

 // Metodo PUT per aggiornare lo stato di una camera e poi salvare l'intero oggetto
    @PutMapping("/room/{nome}/state")
    public String updateCameraStateAndSave(@PathVariable String nome, @RequestBody StateDate statoData) {
        // Ottieni la camera dal RoomService
        ResourceRoom camera = roomService.getCamera(nome);
        
        // Crea un'istanza del Visitor
        VisitorSetState visitor = new VisitorSetState();
        
        // Usa il Visitor per applicare lo stato alla camera
        visitor.visit(camera, statoData);
        
        // Aggiorna l'intera camera nel RoomService
        roomService.updateCamera(nome, camera);
        
        return "Stato della camera con nome: " + nome + " aggiornato con successo e camera salvata.";
    }


    // Metodo DELETE per rimuovere una camera
    @DeleteMapping("/room/{id}")
    public String deleteCamera(@PathVariable String nome) {
        roomService.deleteCamera(nome);
        return "Camera con nome: " + nome + " rimossa con successo.";
    }   
}
