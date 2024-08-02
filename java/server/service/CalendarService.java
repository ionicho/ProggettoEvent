package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import server.AppConfig;
import server.model.*;

/**
 * Classe che estende la classe astratta {@link AbstractService}.
 * Questa classe risponde alle richieste provenienti da CalendarController 
 * e gestisce le operazioni relative alle istanze della classe business 
 * {@link server.model.Calendar}.
 */

@Service
public class CalendarService extends AbstractService <server.model.Calendar>{

    private static final String DBNAME = AppConfig.DATABASE_ROOT_PATH +"Calendario.json";

    public CalendarService(Gson gson) {
        super(AppConfig.DATABASE_ROOT_PATH +"Calendario.json",
        		gson, 
        		new TypeToken<List<server.model.Calendar>>(){}.getType());
    }
   
    public void setCalendario(String nome, LocalDate start, LocalDate end) {
    	for (server.model.Calendar curr : risorse) {
            if (curr.getNome().compareTo(nome)==0) {
            	curr.setCalendario(start, end);
            	salvaNelDB(DBNAME, myGson, risorse);
            }
    	}
    }
    
    public void changeStatoCalendario(String nome, StateDate statoData) {
        server.model.Calendar calendario = getRisorsa(nome);
        VisitorSetState visitor = new VisitorSetState();
        visitor.visit(calendario, statoData);
        if (calendario != null && calendario.getNome() != null && !calendario.getNome().isEmpty()) {
            for (int i = 0; i < risorse.size(); i++) {
                if (risorse.get(i).getNome().equals(nome)) {
                	risorse.set(i, calendario);
                    salvaNelDB(DBNAME, myGson, risorse);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Camera con nome: " + nome + " non trovata.");
    }
    
}