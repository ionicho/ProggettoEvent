package server.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

import server.model.*;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per le CAMERE.
 */

@Service
public class RoomService {

    private static final String DATABASE_FILE = "D:\\UniBG\\Event\\DataBase\\Camere.json";
    private List<Room> camere;
    private LocalDateTypeAdapter localDateTypeAdapter;
    private StateDateTypeAdapter stateDateTypeAdapter;

    public RoomService() {
        this.localDateTypeAdapter = new LocalDateTypeAdapter();
        this.stateDateTypeAdapter = new StateDateTypeAdapter();
        this.camere = caricaCamereDaDB();
    }
    
    // Metodo GET per ottenere tutte le camere
    public List<Room> getCamere() {
    	return camere;
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
            salvaCamereSuDB();
        } else {
            throw new IllegalArgumentException("Camera non valida.");
        }
    }
       
 // Metodo PUT per aggiornare una camera
    public void updateCamera(String nome, Room camera) {
        if (camera != null && camera.getNome() != null && !camera.getNome().isEmpty()) {
            for (int i = 0; i < camere.size(); i++) {
                if (camere.get(i).getNome().equals(nome)) {
                    camere.set(i, camera);
                    salvaCamereSuDB();
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
        salvaCamereSuDB();
    }
    
    private List<Room> caricaCamereDaDB() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .registerTypeAdapter(StateDate.class, stateDateTypeAdapter)
                .create();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<Room>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaCamereSuDB() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .registerTypeAdapter(StateDate.class, stateDateTypeAdapter)
                .setPrettyPrinting()  // inserisce i CR
                .create();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            // Rimuove eventuali oggetti null dalla lista prima di salvarla
            camere.removeAll(Collections.singleton(null));
            pw.println(gson.toJson(camere));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



        
