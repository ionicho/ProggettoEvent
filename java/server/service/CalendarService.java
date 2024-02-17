package server.service;

import com.google.gson.Gson;

import org.springframework.stereotype.Service;
import java.io.*;
import java.time.LocalDate;

import server.AppConfig;
import server.model.Calendar;

@Service
public class CalendarService {

    private static final String DATABASE_FILE = AppConfig.DATABASE_ROOT_PATH +"Calendario.json";
    private Calendar calendario;
    private Gson gson;

    public CalendarService(Gson gson) {
        this.gson = gson;
        this.calendario = caricaCalendarioDaDB();
    }
    
    public Calendar getCalendario() {
        return calendario;
    }
    
    private Calendar caricaCalendarioDaDB() {
        try {
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
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            pw.println(gson.toJson(calendario));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
   }
 
}
