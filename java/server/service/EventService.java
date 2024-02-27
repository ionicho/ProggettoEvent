package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import server.*;
import server.model.*;

/**
 * Classe che estende la classe astratta {@link AbstractService}.
 * Questa classe risponde alle richieste provenienti da EventController 
 * e gestisce le operazioni relative alle istanze della classe business 
 * {@link server.model.Event}.
 * La classe implementa l'interfaccia {@link Subscriber} per ricevere
 * notifiche di cambiamento di disponbilità della struttura.
 */

@Service
public class EventService extends AbstractService <Event> implements Subscriber {

    public EventService(Gson gson) {
        super(AppConfig.DATABASE_ROOT_PATH +"Eventi.json",
            gson, 
            new TypeToken<List<Event>>(){}.getType());
    }
    
    public Event addEvento() {
    	Event evento = new Event("ciao"); //uso il costruttore che usa il singleton
    	risorse.add(evento);
    	salvaNelDB(DBname, myGson, risorse);
    	return evento;
    }

    @Override
    public List<String> changeDisponibilita(List<StateDate> disponibilita) {
        List<String> eventiToBeResc = new ArrayList<>();
        LocalDate oldData;
        for (Event curr : risorse) {
            for(StateDate sd : disponibilita){
                oldData = curr.getData();
                if (oldData == sd.getData() && sd.getStato() == State.CHIUSO){
                    eventiToBeResc.add(sd.getData().toString() + ": " + curr.getNome());
                    curr.setData(null);
                } else {
                    // non fare niente, va bene così
                }   
            }
        }
        salvaNelDB(DBname, myGson, risorse);
        return eventiToBeResc;
    }

    // Metodo GET per ottenere un evento che corrisponde a una data e una sala
    public Event getEventoByDateHall(LocalDate date, String salaName) {  	
        for (Event curr : risorse) {
            if (curr.getData().compareTo(date)==0 && curr.getSala().compareTo(salaName)==0) {
                return curr;
            }
        }
        return null; // Restituisci null se nessun evento corrisponde all'ID fornito
    }

}