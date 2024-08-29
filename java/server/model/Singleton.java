package server.model;

import java.util.HashMap;
import server.service.SingletonService;

/**
 * Questa classe mantiene un contatore per ciascun tipo di risorsa ed è thread-safe.
 * ATTENZIONE: l'invocazione con il tipo di risorsa sbagliato crea un nuovo contatore.
 * Il contatore è una stringa = TipoRisorsa + Integer (formattato a 3 cifre).
 */
public class Singleton implements HasName {

    private static Singleton instance = null;
    private HashMap<String, Integer> counters;
    private String nome = "Singleton";

    // Variabile per il SingletonService
    private static SingletonService singletonService;

    // Costruttore privato
    private Singleton() {
        this.counters = new HashMap<>();
        // Assicurati che singletonService sia stato impostato
        if (singletonService != null) {
            this.counters = singletonService.caricaCountdaDB();
        } else {
            throw new IllegalStateException("SingletonService non è stato inizializzato.");
        }
    }

    // Metodo per ottenere l'istanza singleton
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    // Metodo per impostare SingletonService
    public static void setSingletonService(SingletonService service) {
        singletonService = service;
    }

    private synchronized int getCount(String tipoRisorsa) {
        return counters.getOrDefault(tipoRisorsa, 0);
    }

    private synchronized void incrementCount(String tipoRisorsa) {
        counters.put(tipoRisorsa, getCount(tipoRisorsa) + 1);
    }

    public synchronized String getNext(String tipoRisorsa) {
        incrementCount(tipoRisorsa);
        singletonService.salvaCountsuDB(counters);
        return formatCount(tipoRisorsa, getCount(tipoRisorsa));
    }

    public synchronized String getLast(String tipoRisorsa) {
        return formatCount(tipoRisorsa, getCount(tipoRisorsa));
    }

    private String formatCount(String tipoRisorsa, int count) {
        return tipoRisorsa + String.format("%03d", count);
    }

    public String getNome() {
        return nome;
    }
}
