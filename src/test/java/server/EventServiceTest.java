package server;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import server.service.EventService;
import server.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private List<Event> eventi;

    @Test
    public void testAddEvento() {
        Event evento = eventService.addEvento();
        System.out.println("Evento: " + evento);
        System.out.println("ID dell'evento: " + evento.getId());
        assertNotNull(evento);
        assertNotNull(evento.getId());
    }

    @Test
    public void testUpdateEvento() {
        Event oldEvent = new Event("vecchio");
        oldEvent.setNomeOrg("vecchio nome");
        when(eventi.get(anyInt())).thenReturn(oldEvent);

        Event newEvent = new Event("nuovo");
        newEvent.setNomeOrg("nuovo nome");

        Event updatedEvent = eventService.updateEvento(newEvent);
        assertNotNull(updatedEvent);
        assertEquals("nuovo nome", updatedEvent.getNomeOrganizzatore());
        // Aggiungi ulteriori asserzioni se necessario
    }
}
