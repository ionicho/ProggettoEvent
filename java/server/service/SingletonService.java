package server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import server.AppConfig;

import java.io.*;
import java.util.*;

@Service
public class SingletonService {

    private static final String myColl = "Singleton"; //NOSONAR
    private static final String myDBname = AppConfig.DATABASE_ROOT_PATH + myColl + ".json"; //NOSONAR
    private final MongoTemplate myMongoDB;
    private final boolean useMongoDB;

    // Costruttore per l'iniezione di MongoTemplate
    public SingletonService(MongoTemplate mongoTemplate) {
        this.myMongoDB = mongoTemplate;
        this.useMongoDB = AppConfig.useMongoDB();
    }

    private Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    // Metodo per caricare i contatori da DB
    public HashMap<String, Integer> caricaCountdaDB() {
        if (useMongoDB) {
            MongoCollection<Document> collection = myMongoDB.getDb().getCollection(myColl);   
            List<Document> documents = collection.find().into(new ArrayList<>());

            HashMap<String, Integer> counters = new HashMap<>();
            for (Document doc : documents) {
                String tipoRisorsa = doc.getString("tipoRisorsa");
                Integer count = doc.getInteger("count", 0);
                counters.put(tipoRisorsa, count);
            }
            return counters;
        } else {
            try {
                Gson gson = getGson();
                BufferedReader br = new BufferedReader(new FileReader(myDBname));
                return gson.fromJson(br, new TypeToken<HashMap<String, Integer>>(){}.getType());
            } catch (IOException e) {
                return new HashMap<>();
            }
        }
    }

    // Metodo per salvare i contatori su DB
    public void salvaCountsuDB(Map<String, Integer> counters) {
        if (useMongoDB) {
            MongoCollection<Document> collection = myMongoDB.getDb().getCollection(myColl);   
            collection.drop(); // Opzionale: Pulisce la collezione esistente

            for (Map.Entry<String, Integer> entry : counters.entrySet()) {
                Document doc = new Document("tipoRisorsa", entry.getKey())
                                .append("count", entry.getValue());
                collection.insertOne(doc);
            }
        } else {
            try {
                Gson gson = getGson();
                PrintWriter pw = new PrintWriter(new FileWriter(myDBname));
                pw.println(gson.toJson(counters));
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
