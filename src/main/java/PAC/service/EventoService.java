package PAC.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import PAC.model.Evento;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class EventoService {

    private static final String DATABASE_FILE = "D:\\UniBG\\Event\\DataBase\\ElencoEventi.json";
    private List<Evento> eventi;

    public EventoService() {
        this.eventi = caricaEventiDaDatabase();
    }

    // Metodo GET per ottenere tutti gli eventi
    public List<Evento> getEventi() {
        return eventi;
    }
    
 // Metodo GET per ottenere un singolo evento
    public Evento getEvento(String id) {
    	Evento e=null;
        for (Evento evento : eventi) {
        	e= evento;
            if (evento.getId().equals(id)) {
                return evento;
            }
        }
        return e;
        //return null; // Restituisci null se nessun evento corrisponde all'ID fornito

    }

    // Metodo POST per aggiungere un evento
    public void addEvento(Evento evento) {
        eventi.add(evento);
        salvaEventiSuDatabase();
    }

    // Metodo PUT per aggiornare un evento
    public void updateEvento(String id, Evento evento) {
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

    private List<Evento> caricaEventiDaDatabase() {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<Evento>>(){}.getType());
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
