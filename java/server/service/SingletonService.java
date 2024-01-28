package server.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

/**
 * Questa classe esegue le operazioni di lettura e scrittura
 * del file json che funge da DB per il Singleton.
 */

@Service
public class SingletonService {

    private static final String DATABASE_FILE = "D:\\UniBG\\Event\\DataBase\\Singleton.json";

    // Metodo per caricare i contatori da DB
    public static HashMap<String, Integer> caricaCountDaDB() {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_FILE));
            return gson.fromJson(br, new TypeToken<HashMap<String, Integer>>(){}.getType());
        } catch (IOException e) {
            return new HashMap<>(); // Inizia da un HashMap vuoto se non è possibile caricare da DB
        }
    }

    // Metodo per salvare i contatori su DB
    public static void salvaCountSuDB(Map<String, Integer> counters) {
        try {
            Gson gson = new GsonBuilder()
                .setPrettyPrinting()  // inserisce i CR
                .create();
            PrintWriter pw = new PrintWriter(new FileWriter(DATABASE_FILE));
            pw.println(gson.toJson(counters));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
