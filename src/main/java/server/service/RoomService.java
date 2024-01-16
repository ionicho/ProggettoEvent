package server.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import server.model.*;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class RoomService {

    private static final String DATABASE_FILE = "D:\\UniBG\\Event\\DataBase\\Camere.json";
    private List<ResourceRoom> camere;
    private LocalDateTypeAdapter localDateTypeAdapter;
    private StateDateTypeAdapter stateDateTypeAdapter;

    public RoomService() {
        this.localDateTypeAdapter = new LocalDateTypeAdapter();
        this.stateDateTypeAdapter = new StateDateTypeAdapter();
        this.camere = caricaCamereDaDatabase();
    }
    
    // Metodo GET per ottenere tutti le camere
    public List<ResourceRoom> getCamere() {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, localDateTypeAdapter).create();
        
        String json = gson.toJson(camere);
        return gson.fromJson(json, new TypeToken<List<ResourceRoom>>(){}.getType());
    }

    // Metodo GET per ottenere una singola camera
    public ResourceRoom getCamera(String id) {
    	for (ResourceRoom curr : camere) {
            if (curr.getNome().equals(id)) {
            	ResourceRoom camera = new ResourceRoom(curr.getNome(), curr.getCosto(), curr.getNumeroLetti(), curr.getTipo(), curr.getDisponibilita());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, localDateTypeAdapter).create();
                String json = gson.toJson(camera);
                
                System.out.printf("\n\n%s\n\n", json);
                
                return camera; // Restituisci null se nessuna camera corrisponde al name fornito
            }
        }
        return null;
    }

 // Metodo POST per aggiungere una camera
    public void addCamera(ResourceRoom camera) {
        if (camera != null && camera.getNome() != null && !camera.getNome().isEmpty()) {
            camere.add(camera);
            salvaCamereSuDatabase();
        } else {
            throw new IllegalArgumentException("Camera non valida.");
        }
    }
       
 // Metodo PUT per aggiornare una camera
    public void updateCamera(String nome, ResourceRoom camera) {
        if (camera != null && camera.getNome() != null && !camera.getNome().isEmpty()) {
            for (int i = 0; i < camere.size(); i++) {
                if (camere.get(i).getNome().equals(nome)) {
                    camere.set(i, camera);
                    salvaCamereSuDatabase();
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Camera con nome: " + nome + " non trovata.");
    }

    // Metodo DELETE per rimuovere una camera
    public void deleteCamera(String nome) {
        camere.remove(nome);
        salvaCamereSuDatabase();
    }
    
    private List<ResourceRoom> caricaCamereDaDatabase() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .registerTypeAdapter(StateDate.class, stateDateTypeAdapter)
                .create();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<ResourceRoom>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaCamereSuDatabase() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .registerTypeAdapter(StateDate.class, stateDateTypeAdapter)
                .create();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            // Rimuove eventuali oggetti null dalla lista prima di salvarla
            camere.removeAll(Collections.singleton(null));
            pw.println(gson.toJson(camere));
            pw.close();
        } catch (IOException e) {
            // Log the error or rethrow the exception
            e.printStackTrace();
        }
    }
}
/*
    private List<ResourceRoom> caricaCamereDaDatabase() {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, localDateTypeAdapter).create();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<ResourceRoom>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaCamereSuDatabase() {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, localDateTypeAdapter).create();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            pw.println(gson.toJson(camere));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/


        
