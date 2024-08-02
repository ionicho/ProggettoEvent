package server.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import server.AppConfig;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per il Singleton.
 */

 @Service
 public class SingletonService {
 
    private static final String DBNAME = AppConfig.DATABASE_ROOT_PATH +"Singleton.json";
 
    // Costruttore privato per evitare che venga istanziata
    private SingletonService() {
    }

     private static Gson getGson() {
         return new GsonBuilder()
             .setPrettyPrinting()  // inserisce i CR
             .create();
     }
 
    // Metodo per caricare i contatori da DB
    public static HashMap<String, Integer> caricaCountdaDB() {//NOSONAR 
        try {
            Gson gson = getGson();
            BufferedReader br = new BufferedReader(new FileReader(DBNAME));
            return gson.fromJson(br, new TypeToken<HashMap<String, Integer>>(){}.getType());
        } catch (IOException e) {
            return new HashMap<>(); //Inizia da un HashMap vuoto se non è possibile caricare da DB
        }
    }
 
     // Metodo per salvare i contatori su DB
     public static void salvaCountsuDB(Map<String, Integer> counters) {
         try {
             Gson gson = getGson();
             PrintWriter pw = new PrintWriter(new FileWriter(DBNAME));
             pw.println(gson.toJson(counters));
             pw.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }