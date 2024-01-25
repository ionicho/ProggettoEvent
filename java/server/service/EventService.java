package server.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import server.model.Event;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per gli EVENTI.
 */
@Service
public class EventService {

    private static final String DATABASE_FILE = "D:\\UniBG\\Event\\DataBase\\Eventi.json";
    private List<Event> eventi;

    public EventService() {
        this.eventi = caricaEventiDaDB();
    }

    // Metodo GET per ottenere tutti gli eventi
    public List<Event> getEventi() {
        return eventi;
    }
    
 // Metodo GET per ottenere un singolo evento
    public Event getEvento(String id) {
        for (Event curr : eventi) {
            if (curr.getId().equals(id)) {
                return curr;
            }
        }
        return null; // Restituisci null se nessun evento corrisponde all'ID fornito
    }

    // Metodo POST per aggiungere un evento
    public void addEvento(Event evento) {
        if (evento != null && evento.getId() != null && !evento.getId().isEmpty()) {
    			eventi.add(evento);
    			 salvaEventiSuDB();
    		} else {
                throw new IllegalArgumentException("Evento non valido.");
    		}
    	}

    // Metodo PUT per aggiornare un evento
    public void updateEvento(String id, Event evento) {
        if (evento != null && evento.getId() != null && !evento.getId().isEmpty()) {
        	for (int i = 0; i < eventi.size(); i++) {
	            if (eventi.get(i).getId().compareTo(evento.getId()) ==0 ) {
	                eventi.set(i, evento);
	                salvaEventiSuDB();
	                return;
	            }
        	}
        }
        throw new IllegalArgumentException("Evento con ID: " + evento.getId() + " non trovato.");
    }

    /**
     *  Metodo DELETE per rimuovere un evento, funz lambda imposta da Sonar Lint
      */
    public void deleteEvento(String id) {
      	eventi.removeIf(curr -> curr.getId().equals(id)); 
        salvaEventiSuDB();
    }

    private List<Event> caricaEventiDaDB() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .create();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<Event>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaEventiSuDB() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .setPrettyPrinting()  // inserisce i CR
                .create();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            // Rimuove eventuali oggetti null dalla lista prima di salvarla
            eventi.removeAll(Collections.singleton(null));
            pw.println(gson.toJson(eventi));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
