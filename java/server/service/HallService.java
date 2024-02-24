package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.util.*;
import server.*;
import server.model.*;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per le SALE.
 */

@Service
public class HallService implements Subscriber, RWjson <Hall>{

    private static final String DBname = AppConfig.DATABASE_ROOT_PATH +"Sale.json";
    private List<Hall> sale;
    private final Gson gson;

    public HallService(Gson gson) {
        this.gson = gson;
        Type typeOfT = new TypeToken<List<Room>>(){}.getType();
        this.sale = caricadaDB(DBname, gson, typeOfT);
        System.out.println("Caricate " + sale.size() + sale.toString());
    }

    @Override
    public List<String>  updateState(List<StateDate> disponibilita) {
        List<String> saleToBeResc = new ArrayList<>();
        State oldStato;
        VisitorSetState setVisitor = new VisitorSetState();
        VisitorGetState getVisitor = new VisitorGetState();
        for (Hall curr : sale) {
            for(StateDate sd : disponibilita){
                oldStato = getVisitor.visit(curr, sd);
                if (oldStato == State.PRENOTATA && sd.getStato() == State.CHIUSO){
                    saleToBeResc.add(curr.getNome());
                    setVisitor.visit(curr, sd);
                } else if (oldStato != null && sd.getStato() == State.DISPONIBILE){
                    // non fare niente, va bene così
                } else{ //if oldStato == null
                    setVisitor.visit(curr, sd);           
                }   
            }
        }
        salvaSalesuDB();
        return saleToBeResc;
    }
    
    // Metodo GET per ottenere tutte le sale
    public List<Hall> getSale() {
        return sale;
    }

    // Metodo GET per ottenere una singola sala
    public Hall getSala(String id) {
    	for (Hall curr : sale) {
            if (curr.getNome().compareTo(id)==0) {
                return curr;
            }
        }
        return null;
    }
    
    // Metodo GET per ottenere l'elenco dei nomi dell sale libere in una data specifica
    public List<String> getSaleLibere(LocalDate data) {
        List<String> saleLibere = new ArrayList<>();
        VisitorGetState visitor = new VisitorGetState();
        StateDate sd = new StateDate(data,null);
        for (Hall curr : sale) {
            if (visitor.visit(curr, sd) == State.DISPONIBILE) {
                saleLibere.add(curr.getNome());
            }
        }
        return saleLibere;
    }

 // Metodo POST per aggiungere una sala
    public void addSala(Hall sala) {
        if (sala != null && sala.getNome() != null && !sala.getNome().isEmpty()) {
            sale.add(sala);
            salvaSalesuDB();
        } else {
            throw new IllegalArgumentException("Sala non valida.");
        }
    }
       
 // Metodo PUT per aggiornare lo stato di una sala
    public void updateSala(String nome, StateDate statoData) {
        Hall sala = getSala(nome);
        VisitorSetState visitor = new VisitorSetState();
        visitor.visit(sala, statoData);    
        if (sala != null && sala.getNome() != null && !sala.getNome().isEmpty()) {
            for (int i = 0; i < sale.size(); i++) {
                if (sale.get(i).getNome().equals(nome)) {
                    sale.set(i, sala);
                    System.out.printf("scritto nel file %s\n\n",sale.toString());
                    salvaSalesuDB();
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Sala con nome: " + nome + " non trovata.");
    }

    /**
     *  Metodo DELETE per rimuovere una camera, funz lambda imposta da Sonar Lint
      */
    public void deleteSala(String nome) {
        sale.removeIf(curr-> curr.getNome().equals(nome)); 
        salvaNelDB(DBname, gson, sale);
    }
    
    private void salvaSalesuDB() {
        salvaNelDB(DBname, gson, sale);
    }
}

