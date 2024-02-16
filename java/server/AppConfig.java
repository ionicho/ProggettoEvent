package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import server.service.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class AppConfig {
     /** 
     * I metodi statici possono essere utilizzato anche dai client anche se il server è spento
     */

    /**
     * Directory in cui verranno salvati i file JSON e URL per le chiamate REST
     */
    public static final String DATABASE_ROOT_PATH = "D:\\UniBG\\Event\\DataBase\\";
    public static final String URL = "http://localhost:8080/";

    public static String getURL() {
        return URL;
    }

    /** 
     * Configura un RestTemplate con un GsonHttpMessageConverter
     * che utilizza un Gson con dei TypeAdapter per le classi LocalDate, LocalTime e Integer
     * @return un'istanza di RestTemplate configurata
     */
    @SuppressWarnings("null")
    public static RestTemplate configureRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter());
        Gson gson = gsonBuilder.create();
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(gson);
        restTemplate.getMessageConverters().removeIf(m -> m.getClass().getName().equals(GsonHttpMessageConverter.class.getName()));
        restTemplate.getMessageConverters().add(gsonHttpMessageConverter);
        return restTemplate;
    }

    /**
     * bean di tipo RestTemplate per effettuare le chiamate REST.
     * Iniettato nei servizi che ne avranno bisogno con @Autowired
     * @return un'istanza configurata di RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return configureRestTemplate();
    }

    /**
     * Configura un Gson con dei TypeAdapter per le classi LocalDate, LocalTime e Integer
     * @return un'istanza di Gson configurata
     */
    public static Gson configureGson() {
        return new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
            .create();
    }

    /**
     * bean di tipo Gson per deserializzre i dati ricevuti dalle chiamate REST.
     * Iniettato nei servizi che ne avranno bisogno con @Autowired
     * @return un'istanza configurata di Gson
     */
    @Bean
    public Gson gson() {
        return configureGson();
    }

}