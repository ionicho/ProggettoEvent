package server;

import com.google.gson.*;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.time.*;

import server.adapter.*;

/**
 * Classe di configurazione per l'applicazione
 * Contiene i bean di tipo RestTemplate e Gson
 * usa metodi statici per la configurazione di RestTemplate e Gson
 * perché possono essere utilizzati anche dai client anche se il server è spento
 */

@Configuration
public class AppConfig {

    //  Directory in cui verranno salvati i file JSON e URL per le chiamate REST
    public static final String DATABASE_ROOT_PATH = "D:\\UniBG\\Event\\DataBase\\";
    // base URL per le chiamate REST
    public static final String URL = "http://localhost:8080/";

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

}