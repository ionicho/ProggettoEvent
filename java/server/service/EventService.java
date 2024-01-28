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
        this.eventi = caricaEventiDaDatabase();
    }

    // Metodo GET per ottenere tutti gli eventi
    public List<Event> getEventi() {
        return eventi;
    }
    
 // Metodo GET per ottenere un singolo evento
    public Event getEvento(String id) {
        for (Event curr : eventi) {
        	if (curr.getId().compareTo(id)==0) return curr;
        }
        return null; // Restituisci null se nessun evento corrisponde all'ID fornito
    }

    // Metodo GET per invocare il singleton per poter aggiungere un evento
    public Event addEvento() {
    	Event evento = new Event("ciao"); //uso il costruttore che usa il dingleton
    	eventi.add(evento);
    	salvaEventiSuDatabase();
    	return evento;
    }

    // Metodo PUT per aggiornare un evento
    public Event updateEvento(Event evento) {
        for (int i = 0; i < eventi.size(); i++) {
            if (eventi.get(i).getId().compareTo(evento.getId()) ==0 ) {
            	System.out.printf("SERVICE prima della set %s\n", eventi.get(i).toString());
                eventi.set(i, evento);
                salvaEventiSuDatabase();
            	System.out.printf("SERVICE prima dopo la set %s\n", evento.toString());
            	return evento;
            }
        }
        return null; //non c'è l'elemento da aggiornare!
    }

    /**
     *  Metodo DELETE per rimuovere un evento, funz lambda imposta da Sonar Lint
      */
    public void deleteEvento(String id) {
      	eventi.removeIf(curr -> curr.getId().equals(id)); 
        salvaEventiSuDatabase();
    }

    private List<Event> caricaEventiDaDatabase() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .create();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<Event>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaEventiSuDatabase() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .setPrettyPrinting()  // inserisce i CR
                .create();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            pw.println(gson.toJson(eventi));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
