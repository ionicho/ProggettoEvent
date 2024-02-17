package server;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.google.gson.Gson;

import server.service.EventService;
import server.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private List<Event> eventi;

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = AppConfig.configureGson();
    }

    /**
     * Test per verificare che la serializzazione e 
     * deserializzazione di un oggetto Event funzioni correttamente
     */
    @Test
    void eventGsonTest() {
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

        // Serializzazione dell'oggetto Event in una stringa JSON
        String eventJson = gson.toJson(event);
        // Deserializzazione della stringa JSON in un oggetto Event
        Event deserializedEvent = gson.fromJson(eventJson, Event.class);
        // Verifica che l'oggetto Event deserializzato sia uguale all'oggetto Event originale
        assertEquals(event, deserializedEvent);
    }

    @Test
    void testAddEvento() {
        Event evento = eventService.addEvento();
        System.out.println("Evento: " + evento);
        System.out.println("ID dell'evento: " + evento.getId());
        assertNotNull(evento);
        assertNotNull(evento.getId());
    }

    /**
     * Test per verificare che l'aggiunta di un intervento ad un evento funzioni correttamente
     * @throws Exception se si verifica un errore durante l'aggiunta dell'intervento
     */
    @Test
    void testUpdateEvento() {
        Event oldEvent = new Event("vecchio");
        oldEvent.setNomeOrganizzatore("vecchio nome");
        when(eventi.get(anyInt())).thenReturn(oldEvent);

        Event newEvent = new Event("nuovo");
        newEvent.setNomeOrganizzatore("nuovo nome");

        Event updatedEvent = eventService.updateEvento(newEvent);
        assertNotNull(updatedEvent);
        assertEquals("nuovo nome", updatedEvent.getNomeOrganizzatore());
    }
}
