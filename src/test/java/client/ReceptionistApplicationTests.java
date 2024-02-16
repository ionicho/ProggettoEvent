package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import server.model.*;
import server.service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest(classes = ReceptionistApplication.class)
class ReceptionistApplicationTests {

    private RestTemplate restTemplate;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter());
        gson = gsonBuilder.create();
    }

    @Test
    void restTemplateTest() {
        assertNotNull(restTemplate, "RestTemplate dovrebbe essere inizializzato");
    }

    @Test
    void integerTypeAdapterTest() {
        String json = "{\"costoPartecipazione\":null}";
        Event evento = gson.fromJson(json, Event.class);
        assertEquals(null, evento.getCosto(), "Il costo partecipazione dovrebbe essere null");

        json = "{\"costoPartecipazione\":22}";
        evento = gson.fromJson(json, Event.class);
        assertEquals(22, evento.getCosto(), "Il costo partecipazione dovrebbe essere 22");
    }
}