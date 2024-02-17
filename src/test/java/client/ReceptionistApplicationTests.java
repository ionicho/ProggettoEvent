package client;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import java.time.*;

import server.AppConfig;
import server.model.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ReceptionistApplication.class, server.AppConfig.class})
class ReceptionistApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void restTemplateTest() {
        assertNotNull(restTemplate, "RestTemplate dovrebbe essere inizializzato");
    }

    /**
     * Test per verificare che la deserializzazione di un oggetto Event funzioni correttamente
     * Non setta id, per non incrementare il contatore degli eventi
     * @throws Exception se si verifica un errore durante la deserializzazione
     */
   @Test
    void eventRestTemplateTest() throws Exception {
        Event event = new Event();
        event.setData(LocalDate.now());
        event.setOraInizio(LocalTime.now());
        event.setOraFine(LocalTime.now().plusHours(2));
        event.setNomeOrganizzatore("Organizzatore");
        event.setCostoPartecipazione(100.);
        event.setPartPrevisti(23);
        event.setCatering(TipoCatering.BRUNCH);
        Speech speech1 = new Speech();
        speech1.setTitolo("Titolo1");
        speech1.setRelatore("Relatore1");
        speech1.setDescrizione("Descrizione1");
        event.addIntervento(speech1);
        Speech speech2 = new Speech();
        speech2.setTitolo("Titolo2");
        speech2.setRelatore("Relatore2");
        speech2.setDescrizione("Descrizione2");
        event.addIntervento(speech2);

        Gson gson = AppConfig.configureGson();
        // Serializzazione dell'oggetto Event in una stringa JSON
        String eventJson = gson.toJson(event);
        // Deserializzazione della stringa JSON in un oggetto Event
        Event deserializedEvent = gson.fromJson(eventJson, Event.class);
        // Verifica che l'oggetto Event deserializzato sia uguale all'oggetto Event originale
        assertEquals(event, deserializedEvent);
    }
}