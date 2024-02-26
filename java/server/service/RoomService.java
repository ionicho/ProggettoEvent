package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.util.*;
import server.*;
import server.model.*;

/**
 * Classe che estende la classe astratta {@link AbstractService}.
 * Questa classe risponde alle richieste provenienti da RoomController 
 * e gestisce le operazioni relative alle istanze della classe business 
 * {@link server.model.Room}.
 * La classe implementa l'interfaccia {@link Subscriber} per ricevere
 * notifiche di cambiamento di disponbilità della struttura.
 */

@Service
public class RoomService extends AbstractService <Room> implements Subscriber {

    public RoomService(Gson gson) {
        super(AppConfig.DATABASE_ROOT_PATH +"Camere.json",
            gson, 
            new TypeToken<List<Room>>(){}.getType());
    }
    
    public Room addRisorsa() {
    	Room nuova = new Room("ciao"); //uso il costruttore che usa il singleton
    	risorse.add(nuova);
    	salvaNelDB(DBname, myGson, risorse);
    	return nuova;
    }

    @Override
    public List<String> changeDisponibilita(List<StateDate> disponibilita) {
        List<String> camereToBeResc = new ArrayList<>();
        State oldStato;
        VisitorSetState setVisitor = new VisitorSetState();
        VisitorGetState getVisitor = new VisitorGetState();
        for (Room curr : risorse) {
            for(StateDate sd : disponibilita){
                oldStato = getVisitor.visit(curr, sd);
                if (oldStato == State.PRENOTATA && sd.getStato() == State.CHIUSO){
                    camereToBeResc.add(curr.getNome());
                    setVisitor.visit(curr, sd);
                } else if (oldStato == State.CHIUSO && sd.getStato() == State.DISPONIBILE){
                    setVisitor.visit(curr, sd); // se la camera era chiusa e ora è disponibile
                } else if (oldStato != null && sd.getStato() == State.DISPONIBILE){
                    // non fare niente, va bene così
                } else{ //if oldStato == null
                    setVisitor.visit(curr, sd);           
                }   
            }
        }
        salvaNelDB(DBname, myGson, risorse);
        return camereToBeResc;
    }
    
    // Metodo PUT per aggiornare lo stato di una camera
    public void changeStatoCamera(String nome, StateDate statoData) {
        Room camera = getRisorsa(nome);
        VisitorSetState visitor = new VisitorSetState();
        visitor.visit(camera, statoData);
        if (camera != null) {
            for (int i = 0; i < risorse.size(); i++) {
                if (risorse.get(i).getNome().equals(nome)) {
                    risorse.set(i, camera);
                    salvaNelDB(DBname, myGson, risorse);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Camera con nome: " + nome + " non trovata.");
    }

}


