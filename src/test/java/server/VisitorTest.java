package server;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import server.model.*;

/**
 * Classe di test per i Visitor
 */

@ExtendWith(MockitoExtension.class)
public class VisitorTest {//NOSONAR

    private VisitorSetState visitorSet;
    private VisitorGetState visitorGet;
    private server.model.Calendar calendar;
    private List <StateDate> disp;
    private StateDate stato1;
    private StateDate stato2;

    @BeforeEach
    void setUp() {
        visitorSet = new VisitorSetState();
        visitorGet = new VisitorGetState();
        disp = new ArrayList <>();
		stato1 = new StateDate(LocalDate.now(), State.INUSO);
		stato2 = new StateDate(LocalDate.now().plusDays(1), State.PRENOTATA);
        disp.add(stato1);
        disp.add(stato2);
    }

    Room creaRoom() {
		Room camera = new Room();
		camera.setDisponibilita(disp);
		camera.setNumeroLetti(2);
		camera.setCosto(1234.);
		camera.setTipo(RoomType.SUITE);
		return camera;
    }

    Hall creaSala() {
		Hall sala = new Hall();
		sala.setDisponibilita(disp);
		sala.setNumeroPosti(23);
		sala.setCosto(1234.);
		return sala;
	}

    server.model.Calendar creaCal() {
        server.model.Calendar calendario = new server.model.Calendar();
        calendario.setDisponibilita(disp);
        return calendario;
    }

    @Test
    void testRoom() {
        Room room = creaRoom();
        assertEquals(stato1.getStato(), visitorGet.visit(room, stato1));
        StateDate stato3 = new StateDate(LocalDate.now(), State.DISPONIBILE);
        visitorSet.visit(room, stato3);
        assertEquals(State.DISPONIBILE, visitorGet.visit(room, stato3));
    }

    @Test
    void testHall() {
        Hall hall = creaSala();
        assertEquals(stato1.getStato(), visitorGet.visit(hall, stato1));
        StateDate stato3 = new StateDate(LocalDate.now(), State.DISPONIBILE);
        visitorSet.visit(hall, stato3);
        assertEquals(State.DISPONIBILE, visitorGet.visit(hall, stato3));
    }

    @Test
    void testCalendar() {
        calendar = creaCal();
        assertEquals(stato1.getStato(), visitorGet.visit(calendar, stato1));
        StateDate stato3 = new StateDate(LocalDate.now(), State.DISPONIBILE);
        visitorSet.visit(calendar, stato3);
        assertEquals(State.DISPONIBILE, visitorGet.visit(calendar, stato3));
    }
}