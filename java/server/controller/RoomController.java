package server.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import server.model.Room;
import server.model.StateDate;
import server.model.VisitorSetState;
import server.service.RoomService;

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
        return "Evento aggiunto con successo.";
    }

 // Metodo PUT per aggiornare lo stato di una camera e poi salvare l'intero oggetto
    @PutMapping("/room/{nome}/state")
    public String updateCameraStateAndSave(@PathVariable String nome, @RequestBody StateDate statoData) {
        // Ottieni la camera dal RoomService
        Room camera = roomService.getCamera(nome);
        
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
