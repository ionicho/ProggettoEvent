package server;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.google.gson.Gson;
import server.service.*;
import server.model.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    
    private RoomService roomService;

    @Mock
    private List<Room> sale;
    private Gson gson = AppConfig.configureGson();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomService = new RoomService(gson);
    }

	Room creaRoom() {
		Room camera = new Room();
		List <StateDate> disp = new ArrayList <>();
		StateDate stato1 = new StateDate(LocalDate.now(), State.INUSO);
		StateDate stato2 = new StateDate(LocalDate.now().plusDays(1), State.PRENOTATA);
		disp.add(stato1);
		disp.add(stato2);
		camera.setDisponibilita(disp);
		camera.setNumeroLetti(2);
		camera.setCosto(1234.);
		camera.setTipo(RoomType.SUITE);
		return camera;
	}
	
	@Test
	void GsonTest() {
		Room camera = creaRoom();
		String eventJson = gson.toJson(camera);
		Room deserializedHall = gson.fromJson(eventJson, Room.class);
		assertEquals(camera, deserializedHall);
	}

	@Test
	void loadFromDbTest() {
		List<Room> camere = roomService.getCamere();
		assertFalse(camere.isEmpty());
		for (Room camera : camere) {
			assertNotNull(camera.getNome());
			assertFalse(camera.getNome().isEmpty());
		}
	}

}
