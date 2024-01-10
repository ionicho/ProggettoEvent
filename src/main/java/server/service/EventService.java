package server.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import server.model.Event;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Classe che esegue le richieste relative agli eventi
 */

@Service
public class EventService {

    private static final String DATABASE_FILE = "D:\\UniBG\\Event\\DataBase\\Eventi.json";
    private List<Event> eventi;

    public EventService() {
        this.eventi = caricaEventiDaDatabase();
    }

    // Metodo GET per ottenere tutti gli eventi
    public List<Event> getEventi() {
        return eventi;
    }
    
 // Metodo GET per ottenere un singolo evento
    public Event getEvento(String id) {
    	Event e=null;
        for (Event curr : eventi) {
        	e= curr;
            if (curr.getId().equals(id)) {
                return curr;
            }
        }
        return e;
        //return null; // Restituisci null se nessun evento corrisponde all'ID fornito

    }

    // Metodo POST per aggiungere un evento
    public void addEvento(Event evento) {
        eventi.add(evento);
        salvaEventiSuDatabase();
    }

    // Metodo PUT per aggiornare un evento
    public void updateEvento(String id, Event evento) {
        for (int i = 0; i < eventi.size(); i++) {
            if (eventi.get(i).getId().equals(id)) {
                eventi.set(i, evento);
                salvaEventiSuDatabase();
                return;
            }
        }
        throw new IllegalArgumentException("Evento con ID: " + id + " non trovato.");
    }

    // Metodo DELETE per rimuovere un evento
    public void deleteEvento(String id) {
        eventi.remove(id);
        salvaEventiSuDatabase();
    }

    private List<Event> caricaEventiDaDatabase() {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<Event>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaEventiSuDatabase() {
        try {
            Gson gson = new Gson();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            pw.println(gson.toJson(eventi));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
