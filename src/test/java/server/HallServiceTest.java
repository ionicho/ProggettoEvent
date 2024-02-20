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
public class HallServiceTest {
    
    private HallService hallService;

    @Mock
    private List<Hall> sale;
    private Gson gson = AppConfig.configureGson();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hallService = new HallService(gson);
    }
	
	Hall creaSala() {
		Hall sala = new Hall();
		List <StateDate> disp = new ArrayList <>();
		StateDate stato1 = new StateDate(LocalDate.now(), State.INUSO);
		StateDate stato2 = new StateDate(LocalDate.now().plusDays(1), State.PRENOTATA);
		disp.add(stato1);
		disp.add(stato2);
		sala.setDisponibilita(disp);
		sala.setNumeroPosti(23);
		sala.setCosto(1234.);
		return sala;
	}

	@Test
	void GsonTest() {
		Hall sala = creaSala();
		String eventJson = gson.toJson(sala);
		Hall deserializedHall = gson.fromJson(eventJson, Hall.class);
		assertEquals(sala, deserializedHall);
	}

	@Test
	void loadFromDbTest() {
		List<Hall> sale = hallService.getSale();
		assertFalse(sale.isEmpty());
		for (Hall sala : sale) {
			assertNotNull(sala.getNome());
			assertFalse(sala.getNome().isEmpty());
		}
	}

}
