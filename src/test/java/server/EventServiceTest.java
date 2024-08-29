package server;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.google.gson.Gson;
import server.service.*;
import server.model.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest { //NOSONAR

    private EventService eventService;

    @Mock
    private List<Event> eventi;
    private Gson gson = AppConfig.configureGson();
    private MongoTemplate mongoDB= AppConfig.configureMongoDB();

    @BeforeEach
    void setUp() {
        eventService = new EventService(gson, mongoDB);
    }

    Event creaEvent(){
        Event event = new Event();
        event.setData(LocalDate.now());
        event.setOraInizio(LocalTime.now());
        event.setOraFine(LocalTime.now().plusHours(2));
        event.setNomeOrganizzatore("Organizzatore");
        event.setCostoPartecipazione(100.);
        event.setPartPrevisti(23);
        event.setCatering(CateringType.BRUNCH);
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
        return event;
    }

    @Test
    void GsonTest() {
        Event event = creaEvent();
        String eventJson = gson.toJson(event);
        Event deserializedEvent = gson.fromJson(eventJson, Event.class);
        assertEquals(event, deserializedEvent);
    }

    @Test
	void loadFromDbTest() {
		eventi = eventService.getRisorse();
		assertFalse(eventi.isEmpty());
		for (Event evento : eventi) {
			assertNotNull(evento.getNome());
			assertFalse(evento.getNome().isEmpty());
		}
	}
    
}
