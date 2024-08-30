package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalTime;

import server.adapter.DoubleAdapter;
import server.adapter.IntegerAdapter;
import server.adapter.LocalDateAdapter;
import server.adapter.LocalTimeAdapter;

/**
 * Classe di configurazione per l'applicazione
 * Contiene i bean di tipo RestTemplate e Gson
 * usa metodi statici per la configurazione di RestTemplate e Gson
 * perché possono essere utilizzati anche dai client anche se il server è spento
 */

@Configuration
public class AppConfig {

    //  Directory in cui verranno salvati i file JSON e URL per le chiamate REST
    public static final String DATABASE_ROOT_PATH = "D:\\UniBG\\38090 PAC\\EventDB\\DataBase\\";  // NOSONAR
    // base URL per le chiamate REST
    public static final String URL = "http://localhost:8080/";
    // Configurazione MongoDB
    //private static final String MONGODB_URI = "mongodb://localhost:27017/Event";
    private static final String MONGODB_URI = "mongodb+srv://XXXXXXXXXXXXX@clusterfree.b0tyh.mongodb.net/?retryWrites=true&w=majority&appName=ClusterFree";
    private static final boolean USE_MONGODB = true; // Cambia a false se non vuoi usare MongoDB
    // Data iniziale per la creazione di eventi
    public static final LocalDate START_DATE = LocalDate.of(2024, 1,1);

    
    public static String getURL() {
        return URL;
    }

    /** 
     * Configura un RestTemplate con un GsonHttpMessageConverter
     * che utilizza un Gson con dei TypeAdapter per le classi 
     * LocalDate, LocalTime, Integer e Double
     * @return un'istanza di RestTemplate configurata
     */
    @SuppressWarnings("null")
    public static RestTemplate configureRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Integer.class, new IntegerAdapter());
        gsonBuilder.registerTypeAdapter(Double.class, new DoubleAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        Gson gson = gsonBuilder.create();
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(gson);
        restTemplate.getMessageConverters().removeIf(m -> GsonHttpMessageConverter.class.isAssignableFrom(m.getClass()));
        restTemplate.getMessageConverters().add(gsonHttpMessageConverter);
        return restTemplate;
    }

    /**
     * bean di tipo RestTemplate per effettuare le chiamate REST.
     * Iniettato nei servizi che ne avranno bisogno con @Autowired
     * @return un'istanza configurata di RestTemplate
     */
    @Bean
    RestTemplate restTemplate() {
        return configureRestTemplate();
    }

    /**
     * Configura un Gson con dei TypeAdapter per le classi 
     * LocalDate, LocalTime, Integer e Double
     * @return un'istanza di Gson configurata
     */
    public static Gson configureGson() {
        return new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Integer.class, new IntegerAdapter())
            .registerTypeAdapter(Double.class, new DoubleAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();
    }

    /**
     * bean di tipo Gson per deserializzre i dati ricevuti dalle chiamate REST.
     * Iniettato nei servizi che ne avranno bisogno con @Autowired
     * @return un'istanza configurata di Gson
     */
    @Bean
    Gson gson() {
        return configureGson();
    }
    
    public static Boolean useMongoDB() {
    		return USE_MONGODB;
    }
    
    public static MongoTemplate configureMongoDB() {
    	MongoClient mongoClient = MongoClients.create(MONGODB_URI); // Creazione di MongoClient
    	return new MongoTemplate(mongoClient, "Event"); // Nome del database
    }

    @Bean
	MongoTemplate mongoTemplate() {
    	return configureMongoDB();
    }

}
