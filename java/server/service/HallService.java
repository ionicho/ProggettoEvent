package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;
import server.AppConfig;
import server.model.*;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per le SALE.
 */

@Service
public class HallService {

    private static final String DATABASE_FILE = AppConfig.DATABASE_ROOT_PATH +"Sale.json";
    private List<Hall> sale;
    private final Gson gson;

    public HallService(Gson gson) {
        this.gson = gson;
        this.sale = caricaSaledaDB();
    }
    
    // Metodo GET per ottenere tutte le sale
    public List<Hall> getSale() {
        String json = gson.toJson(sale);
        return gson.fromJson(json, new TypeToken<List<Room>>(){}.getType());
    }

    // Metodo GET per ottenere una singola sala
    public Hall getSala(String id) {
    	for (Hall curr : sale) {
            if (curr.getNome().equals(id)) {
            	return new Hall(curr.getNome(), curr.getCosto(), curr.getNumeroPosti(), curr.getDisponibilita());
            }
        }
        return null;
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
        salvaSalesuDB();
    }
    
    private List<Hall> caricaSaledaDB() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<List<Hall>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvaSalesuDB() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            sale.removeAll(Collections.singleton(null));
            pw.println(gson.toJson(sale));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

