package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.util.*;
import server.*;
import server.model.*;
import java.lang.reflect.Type;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per le CAMERE.
 */

@Service
public class RoomService implements Subscriber, RWjson <Room> {

    private static final String DBname = AppConfig.DATABASE_ROOT_PATH +"Camere.json";
    private List<Room> camere;
    private final Gson gson;

    public RoomService(Gson gson) {
        this.gson = gson;
        Type typeOfT = new TypeToken<List<Room>>(){}.getType();
        this.camere = caricadaDB(DBname, gson, typeOfT);
        System.out.println("Caricate " + camere.size() + camere.toString());
    }

    @Override
    public List<String> updateState(List<StateDate> disponibilita) {
        List<String> camereToBeResc = new ArrayList<>();
        State oldStato;
        VisitorSetState setVisitor = new VisitorSetState();
        VisitorGetState getVisitor = new VisitorGetState();
        for (Room curr : camere) {
            for(StateDate sd : disponibilita){
                oldStato = getVisitor.visit(curr, sd);
                if (oldStato == State.PRENOTATA && sd.getStato() == State.CHIUSO){
                    camereToBeResc.add(curr.getNome());
                    setVisitor.visit(curr, sd);
                } else if (oldStato != null && sd.getStato() == State.DISPONIBILE){
                    // non fare niente, va bene così
                } else{ //if oldStato == null
                    setVisitor.visit(curr, sd);           
                }   
            }
        }
        salvaCameresuDB();
        return camereToBeResc;
    }
    
    // Metodo GET per ottenere tutte le camere
    public List<Room> getCamere() {
        return camere;
    }

    // Metodo GET per ottenere una singola camera
    public Room getCamera(String id) {
        for (Room curr : camere) {
            if (curr.getNome().compareTo(id)==0) {
                return curr;
            }
        }
        return null;
    }

 // Metodo POST per aggiungere una camera
    public void addCamera(Room camera) {
        if (camera != null && camera.getNome() != null && !camera.getNome().isEmpty()) {
            camere.add(camera);
            salvaCameresuDB();
        } else {
            throw new IllegalArgumentException("Camera non valida.");
        }
    }
       
 // Metodo PUT per aggiornare lo stato di una camera
    public void updateCamera(String nome, StateDate statoData) {
        Room camera = getCamera(nome);
        VisitorSetState visitor = new VisitorSetState();
        visitor.visit(camera, statoData);
        if (camera != null && camera.getNome() != null && !camera.getNome().isEmpty()) {
            for (int i = 0; i < camere.size(); i++) {
                if (camere.get(i).getNome().equals(nome)) {
                    camere.set(i, camera);
                    salvaCameresuDB();
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Camera con nome: " + nome + " non trovata.");
    }

    public void deleteCamera(String nome) {
        camere.removeIf(curr-> curr.getNome().equals(nome)); 
        salvaNelDB(DBname, gson, camere);
    }
    
    private void salvaCameresuDB() {
        salvaNelDB(DBname, gson, camere);
    }
}


