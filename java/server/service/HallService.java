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
 * Questa classe risponde alle richieste provenienti da HallController 
 * e gestisce le operazioni relative alle istanze della classe business 
 * {@link server.model.Hall}.
 * La classe implementa l'interfaccia {@link Subscriber} per ricevere
 * notifiche di cambiamento di disponbilità della struttura.
 */

@Service
public class HallService extends AbstractService <Hall> implements Subscriber {

    public HallService(Gson gson) {
        super(AppConfig.DATABASE_ROOT_PATH +"Sale.json",
        		gson, 
        		new TypeToken<List<Hall>>(){}.getType());
    }

    public Hall addRisorsa() {
    	Hall nuova = new Hall("ciao"); //uso il costruttore che usa il singleton
    	risorse.add(nuova);
    	salvaNelDB(DBname, myGson, risorse);
    	return nuova;
    }

    @Override
    public List<String> changeDisponibilita(List<StateDate> disponibilita) {
        List<String> saleToBeResc = new ArrayList<>();
        State oldStato;
        VisitorSetState setVisitor = new VisitorSetState();
        VisitorGetState getVisitor = new VisitorGetState();
        for (Hall curr : risorse) {
            for(StateDate sd : disponibilita){
                oldStato = getVisitor.visit(curr, sd);
                if (oldStato == State.PRENOTATA && sd.getStato() == State.CHIUSO){
                    saleToBeResc.add(curr.getNome());
                    setVisitor.visit(curr, sd);
                } else if (oldStato == State.CHIUSO && sd.getStato() == State.DISPONIBILE){
                    setVisitor.visit(curr, sd); // se la sala era chiusa e ora è disponibile
                } else if (oldStato != null && sd.getStato() == State.DISPONIBILE){
                    // non fare niente, va bene così
                } else{ //if oldStato == null
                    setVisitor.visit(curr, sd);           
                }   
            }
        }
        salvaNelDB(DBname, myGson, risorse);
        return saleToBeResc;
    }


    // Metodo PUT per aggiornare lo stato di una sala
    public void changeStatoSala(String nome, StateDate statoData) {
        Hall sala = getRisorsa(nome);
        VisitorSetState visitor = new VisitorSetState();
        visitor.visit(sala, statoData);    
        if (sala != null && sala.getNome() != null && !sala.getNome().isEmpty()) {
            for (int i = 0; i < risorse.size(); i++) {
                if (risorse.get(i).getNome().equals(nome)) {
                    risorse.set(i, sala);
                    salvaNelDB(DBname, myGson, risorse);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Sala con nome: " + nome + " non trovata.");
    }
    
    // Metodo GET per ottenere l'elenco dei nomi dell sale libere in una data specifica
    public List<String> getSaleLibere(LocalDate data) {
        List<String> saleLibere = new ArrayList<>();
        VisitorGetState visitor = new VisitorGetState();
        StateDate sd = new StateDate(data,null);
        for (Hall curr : risorse) {
            if (visitor.visit(curr, sd) == State.DISPONIBILE) {
                saleLibere.add(curr.getNome());
            }
        }
        return saleLibere;
    }

}

