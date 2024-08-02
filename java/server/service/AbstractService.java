package server.service;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.io.*;
import java.util.*;
import server.model.*;

/**
 * Superclasse per i service che gestiscono le risorse e rispondono ai controller.
 * @param <T> tipo della risorsa
 * @param <Type> tipo della lista di risorse
 * getRisorse() restituisce la lista delle risorse
 * getRisorsa(String id) restituisce la risorsa con l'id specificato
 * deleteRisorsa(String id) elimina la risorsa con l'id specificato
 * updateRisorsa(T nuova) aggiorna la risorsa con i dati della risorsa passata
 * caricadaDB(String myDBname, Gson myGson, Type typeOfT) carica la lista di risorse dal DB
 * salvaNelDB(String myDBname, Gson myGson, List<T> myList) salva la lista di risorse nel DB
 * salvaNelDB(String myDBname, Gson myGson, T myObj) salva la risorsa nel DB
 */

public abstract class AbstractService <T extends HasName>{

    protected String myDBname;
    protected List<T> risorse;
    protected Gson myGson;

    protected AbstractService(String myDBname, Gson myGson, Type typeOfT) {
    	this.myDBname = myDBname;
        this.myGson = myGson;
        this.risorse = caricadaDB(myDBname, myGson, typeOfT);
    }

    public List<T> getRisorse() {
        return risorse;
    }

    public T getRisorsa(String id) {
        for (T curr : risorse) {
            if (curr.getNome().compareTo(id)==0) {
                return curr;
            }
        }
        return null;
    }
    
    public void deleteRisorsa(String id) {
        for (T curr : risorse) {
            if (curr.getNome().compareTo(id)==0) {
                risorse.remove(curr);
                salvaNelDB(myDBname, myGson, risorse);
                return;
            }
        }
        throw new IllegalArgumentException("Risorsa non esistente");
    }
    
    public T updateRisorsa(T nuova) {
        for (T curr : risorse) {
            if (curr.getNome().compareTo(nuova.getNome())==0) {
                risorse.remove(curr);
                risorse.add(nuova);
                salvaNelDB(myDBname, myGson, risorse);
                return nuova;
            }
        }
        throw new IllegalArgumentException("Risorsa non esistente");
    }

    public List<T> updateRisorse(List<T> nuove) {
        risorse = nuove;
        salvaNelDB(myDBname, myGson, risorse);
        return risorse;
    }

    public List<T> caricadaDB(String myDBname, Gson myGson, Type typeOfT) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(myDBname));
            return myGson.fromJson(br, typeOfT);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void salvaNelDB(String myDBname, Gson myGson, List<T> myList) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(myDBname));
            myList.removeAll(Collections.singleton(null));
            pw.println(myGson.toJson(myList));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvaNelDB(String myDBname, Gson myGson, T myObj) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(myDBname));
            pw.println(myGson.toJson(myObj));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}