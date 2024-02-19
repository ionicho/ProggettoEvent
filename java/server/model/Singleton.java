package server.model;

import java.util.HashMap;

import server.service.*;

/**
 * Questa classe mantiene un contatore per ciascun tipo di risorsa ed è threat safe.
 * ATTENZIONE: l'invocazione con la il tipo di risorsa sbagliato crea un nuovo contatore
 * Il contatore è una stringa = TipoRisorsa + Integer (formattato a 3 cifre)
 */

public class Singleton {

    private static Singleton instance = null;
    private HashMap<String, Integer> counters;

    private Singleton() {
        this.counters = new HashMap<>();
        // Carica lo stato da disco
        this.counters = SingletonService.caricaCountdaDB();
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private synchronized int getCount(String tipoRisorsa) {
        return counters.getOrDefault(tipoRisorsa, 0);
    }

    private synchronized void incrementCount(String tipoRisorsa) {
        counters.put(tipoRisorsa, getCount(tipoRisorsa) + 1);
    }
    
    public synchronized String getNext(String tipoRisorsa) {
        incrementCount(tipoRisorsa);
        SingletonService.salvaCountsuDB(counters);
        return formatCount(tipoRisorsa, getCount(tipoRisorsa));
    }
    
    public synchronized String getLast(String tipoRisorsa) {
        return formatCount(tipoRisorsa, getCount(tipoRisorsa));
    }

    private String formatCount(String tipoRisorsa, int count) {
        return tipoRisorsa + String.format("%03d", count);
    }
}