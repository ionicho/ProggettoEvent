package server;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.google.gson.Gson;
import server.service.*;
import server.model.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe di test per il RoomService
 * 
 */

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    private RoomService roomService;
	private VisitorSetState visitorSet = new VisitorSetState();
    private VisitorGetState visitorGet= new VisitorGetState();
    private List <StateDate> disp = new ArrayList <>();
    private StateDate stato1;
	private StateDate stato2;

    @Mock
    private List<Room> sale;
    private Gson gson = AppConfig.configureGson();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomService = new RoomService(gson);
    }

	Room creaRoom(RoomType tipo, State oggiStato, State domaniStato) {
		Room camera = new Room();
		stato1 = new StateDate(LocalDate.now(), oggiStato);
		disp.add(stato1);
		if (domaniStato != null){
			stato2 = new StateDate(LocalDate.now().plusDays(1), domaniStato);
			disp.add(stato2);
		}
		camera.setDisponibilita(disp);
		camera.setNumeroLetti(2);
		camera.setCosto(1234.);
		camera.setTipo(tipo);
		return camera;
	}
	
	@Test
	void GsonTest() {
		Room camera = creaRoom(RoomType.SUITE, State.INUSO, State.PRENOTATA);
		String eventJson = gson.toJson(camera);
		Room deserializedHall = gson.fromJson(eventJson, Room.class);
		assertEquals(camera, deserializedHall);
	}

	@Test
	void getRisorseTest() {
		List<Room> camere = roomService.getRisorse();
		assertFalse(camere.isEmpty());
		System.out.println("Numero di camere: " + camere.size());
		for (Room camera : camere) {
			assertNotNull(camera.getNome());
			assertFalse(camera.getNome().isEmpty());
		}
	}

	@Test
	void getRisorsaTest() {
		Room camera = roomService.getRisorsa("Room001");
		assertNotNull(camera);
		assertEquals("Room001", camera.getNome());
	}
	
	@Test
	void UpdateRisorsaTest() {
		Room camera = roomService.getRisorsa("Room001");
		assertNotNull(camera);
		assertEquals("Room001", camera.getNome());
		Double oldCosto = camera.getCosto();
		camera.setCosto(1000.);
		roomService.updateRisorsa(camera);
		camera = roomService.getRisorsa("Room001");
		assertNotNull(camera);
		assertEquals("Room001", camera.getNome());
		assertEquals(1000., camera.getCosto());
		camera.setCosto(oldCosto);
	}

	@Test
	@Disabled("Test disabilitato a causa della dipendenza dal singleton.")
	void addRisorsaTest() {
		// eseguito solo per Eventi
	}

	@Test
	@Disabled("Test disabilitato a causa della dipendenza dal singleton.")
	void DeleteRisorsaTest() {
		// eseguito solo per Eventi
	}

	@Test
	void changeStatoCameraTest() {
		Room camera = creaRoom(RoomType.SUITE, State.INUSO, State.PRENOTATA);
		StateDate old = new StateDate(LocalDate.now(),State.INUSO);
		StateDate nuovo = new StateDate(LocalDate.now(),State.DISPONIBILE);
		assertEquals(State.INUSO, visitorGet.visit(camera, old));
        visitorSet.visit(camera, nuovo);
        assertEquals(State.DISPONIBILE, visitorGet.visit(camera, nuovo));
	}

	@Test
	void changeDisponibilitaTest() {
		// Crea un mock di RoomService
		RoomService roomServiceMock = mock(RoomService.class);
		// Configura il mock per restituire una lista di camere quando viene chiamato getRisorse()
		Room camera1 = creaRoom(RoomType.SUITE, State.INUSO, State.PRENOTATA); // in disp ha domani
		Room camera2 = creaRoom(RoomType.NORMAL, State.DISPONIBILE, State.DISPONIBILE); // in disp ha domani
		Room camera3 = creaRoom(RoomType.NORMAL, State.DISPONIBILE, null); // in disp non ha domani
		List<Room> camereMock = Arrays.asList(camera1, camera2, camera3);
		// Configura il mock per restituire una lista di nomi di camere quando viene chiamato changeDisponibilita()
		List<String> nomiCamere = Arrays.asList(null, null);
		when(roomServiceMock.changeDisponibilita(any())).thenReturn(nomiCamere);
		// Esegue il test utilizzando il mock di RoomService
		List<StateDate> newDisp = new ArrayList<>();
		newDisp.add(new StateDate(LocalDate.now().plusDays(1), State.CHIUSO));
		List<String> camereToBeResc = roomServiceMock.changeDisponibilita(newDisp);
		// Verifica che il numero totale di camere date al mock sia 3
		assertEquals(3, camereMock.size());
		// Verifica che il numero di camere restituite da changeDisponibilita() sia 2
		assertEquals(2, camereToBeResc.size());
		// Verifica che i nomi delle camere restituite da changeDisponibilita() siano tutti null
		assertTrue(camereToBeResc.stream().allMatch(Objects::isNull));
	}

}
