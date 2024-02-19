package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import server.AppConfig;
import server.model.*;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per le CAMERE.
 */

@Service
public class RoomService {

    private static final String DATABASE_FILE = AppConfig.DATABASE_ROOT_PATH +"Camere.json";
    private List<Room> camere;
    private final Gson gson;

    public RoomService(Gson gson) {
        this.gson = gson;
        this.camere = caricaCameredaDB();
    }
    
    // Metodo GET per ottenere tutte le camere
    public List<Room> getCamere() {
        String json = gson.toJson(camere);
        return gson.fromJson(json, new TypeToken<List<Room>>(){}.getType());
    }

    // Metodo GET per ottenere una singola camera
    public Room getCamera(String id) {
    	for (Room curr : camere) {
            if (curr.getNome().equals(id)) {
            	return new Room(curr.getNome(), curr.getCosto(), curr.getNumeroLetti(), curr.getTipo(), curr.getDisponibilita());
            }
        }
        return null;
    }

 // Metodo POST per aggiungere una camera
    public void addCamera(Room camera) {
        if (camera != null && camera.getNome() != null && !camera.getNome().isEmpty()) {
            camere.add(camera);
            salvaCameresuDB();
        } else {
            throw new IllegalArgumentException("Camera non valida.");
        }
    }
       
 // Metodo PUT per aggiornare lo stato di una camera
    public void updateCamera(String nome, StateDate statoData) {
        Room camera = getCamera(nome);
        VisitorSetState visitor = new VisitorSetState();
        visitor.visit(camera, statoData);
        if (camera != null && camera.getNome() != null && !camera.getNome().isEmpty()) {
            for (int i = 0; i < camere.size(); i++) {
                if (camere.get(i).getNome().equals(nome)) {
                    camere.set(i, camera);
                    salvaCameresuDB();
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Camera con nome: " + nome + " non trovata.");
    }

    /**
     *  Metodo DELETE per rimuovere una camera, funz lambda imposta da Sonar Lint
      */
    public void deleteCamera(String nome) {
    		camere.removeIf(curr-> curr.getNome().equals(nome)); 
        salvaCameresuDB();
    }
    
    private List<Room> caricaCameredaDB() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<Room>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaCameresuDB() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            camere.removeAll(Collections.singleton(null));
            pw.println(gson.toJson(camere));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
