package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.lang.reflect.Type;
import java.util.*;
import server.*;
import server.model.*;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per gli EVENTI.
 */
@Service
public class EventService implements Subscriber, RWjson <Event>{

    private static final String DBname = AppConfig.DATABASE_ROOT_PATH +"Eventi.json";
    private List<Event> eventi;
    private final Gson gson;

    public EventService(Gson gson) {
        this.gson = gson;
        Type typeOfT = new TypeToken<List<Event>>(){}.getType();
        this.eventi = caricadaDB(DBname, gson, typeOfT);
        System.out.println("Caricate " + eventi.size() + eventi.toString());
    }

    @Override
    public List<String>  updateState(List<StateDate> disponibilita) {
        List<String> eventiToBeResc = new ArrayList<>();
        LocalDate oldData;
        for (Event curr : eventi) {
            for(StateDate sd : disponibilita){
                oldData = curr.getData();
                if (oldData == sd.getData() && sd.getStato() == State.CHIUSO){
                    eventiToBeResc.add(curr.getId());
                    curr.setData(null);
                } else {
                    // non fare niente, va bene così
                }   
            }
        }
        salvaEventisuDB();
        return eventiToBeResc;
    }

    // Metodo GET per ottenere tutti gli eventi
    public List<Event> getEventi() {
        return eventi;
    }
    
    // Metodo GET per ottenere un singolo evento
    public Event getEvento(String id) {  	
        for (Event curr : eventi) {
        	if (curr.getId().compareTo(id)==0) {
        		return curr;
        	}
        }
        return null; // Restituisci null se nessun evento corrisponde all'ID fornito
    }

    // Metodo GET per ottenere un evento che corrisponde a una data e una sala
    public Event getEventoByDateHall(LocalDate date, String salaName) {  	
        for (Event curr : eventi) {
            if (curr.getData().compareTo(date)==0 && curr.getSala().compareTo(salaName)==0) {
                return curr;
            }
        }
        return null; // Restituisci null se nessun evento corrisponde all'ID fornito
    }


    // Metodo GET per invocare il singleton per poter aggiungere un evento
    public Event addEvento() {
    	Event evento = new Event("ciao"); //uso il costruttore che usa il singleton
    	eventi.add(evento);
    	salvaEventisuDB();
    	return evento;
    }

    // Metodo PUT per aggiornare un evento
    public Event updateEvento(Event evento) {
        for (int i = 0; i < eventi.size(); i++) {
            if (eventi.get(i).getId().compareTo(evento.getId()) ==0 ) {
            	System.out.printf("\n\nEVENT SERVICE richiesta di UpdateEvento %s\n", eventi.get(i).toString());
                eventi.set(i, evento);
                salvaEventisuDB();
            	System.out.printf("EVENT SERVICE risposta inviata %s\n\n", evento.toString());
            	return evento;
            }
        }
        return null; //non c'è l'elemento da aggiornare!
    }

    public void deleteEvento(String id) {
      	eventi.removeIf(curr -> curr.getId().equals(id)); 
        salvaEventisuDB();
    }

    private void salvaEventisuDB() {
        salvaNelDB(DBname, gson, eventi);
    }
}