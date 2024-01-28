package server.service;

import com.google.gson.*;
import org.springframework.stereotype.Service;
import java.io.*;
import java.time.LocalDate;

import server.model.*;

@Service
public class CalendarService {

    private static final String DATABASE_FILE = "D:\\UniBG\\Event\\DataBase\\Calendario.json";
    private Calendar calendario;
    private LocalDateTypeAdapter localDateTypeAdapter;
    private StateDateTypeAdapter stateDateTypeAdapter;

    public CalendarService() {
        this.localDateTypeAdapter = new LocalDateTypeAdapter();
        this.stateDateTypeAdapter = new StateDateTypeAdapter();
        
        this.calendario = caricaCalendarioDaDB();
    }
    
    public Calendar getCalendario() {
    	return calendario;
    }
    
    private Calendar caricaCalendarioDaDB() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .registerTypeAdapter(StateDate.class, stateDateTypeAdapter)
                .create();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, Calendar.class);
        } catch (IOException e) {
        	System.out.printf("ARRRRG CALENDARIO VUOTO \n");
            return null; // ritorna null se il file non esiste
        }
    }
   
    public void setCalendario(LocalDate start, LocalDate end) {
    	System.out.printf("SERVICE PRESENTE \n");
    	calendario.setCalendario(start, end);
    	salvaCalendarioSuDB();
    }
    
    public void salvaCalendarioSuDB() {
        try {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateTypeAdapter)
                .registerTypeAdapter(StateDate.class, stateDateTypeAdapter)
                .setPrettyPrinting()  // inserisce i CR
                .create();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            pw.println(gson.toJson(calendario));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
   }
    
}

