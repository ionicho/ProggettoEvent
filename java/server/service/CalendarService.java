package server.service;

import com.google.gson.Gson;

import org.springframework.stereotype.Service;
import java.io.*;
import java.time.LocalDate;

import server.AppConfig;
import server.model.Calendar;

@Service
public class CalendarService implements RWjson<Calendar>{

    private static final String DBname = AppConfig.DATABASE_ROOT_PATH +"Calendario.json";
    private Calendar calendario;
    private Gson gson;

    public CalendarService(Gson gson) {
        this.gson = gson;
        this.calendario = caricaCalendariodaDB();
    }
    
    public Calendar getCalendario() {
        return calendario;
    }
    
    private Calendar caricaCalendariodaDB() {
            try {
                BufferedReader br = new BufferedReader(new FileReader(DBname));
                return gson.fromJson(br, Calendar.class);
            } catch (IOException e) {
                System.out.printf("ARRRRG CALENDARIO VUOTO \n");
                return null; // ritorna null se il file non esiste
            }
    }
   
    public void setCalendario(LocalDate start, LocalDate end) {
        calendario.setCalendario(start, end);
        salvaNelDB(DBname, gson, calendario);
    }
    
    public void salvaCalendariosuDB() {
        salvaNelDB(DBname, gson, calendario);
   }
 
}