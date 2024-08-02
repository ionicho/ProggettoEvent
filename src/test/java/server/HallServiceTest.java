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

/**
 * Questa classe contiene i test per i medodi che ha solo
 * la classe {@link HallService}, per gli altri metodi
 * si veda {@link RoomServiceTest}.
 */

@ExtendWith(MockitoExtension.class)
public class HallServiceTest {//NOSONAR
    
    private HallService hallService;
	private List <StateDate> disp = new ArrayList <>();
    private StateDate stato1;
	private StateDate stato2;

    @Mock
    private List<Hall> sale;
    private Gson gson = AppConfig.configureGson();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hallService = new HallService(gson);
    }
	
	Hall creaSala(Integer nPosti, State oggiStato, State domaniStato) {
		Hall sala = new Hall();
		stato1 = new StateDate(LocalDate.now(), oggiStato);
		disp.add(stato1);
		if (domaniStato != null){
			stato2 = new StateDate(LocalDate.now().plusDays(1), domaniStato);
			disp.add(stato2);
		}
		sala.setDisponibilita(disp);
		sala.setNumeroPosti(nPosti);
		sala.setCosto(1234.);
		return sala;
	}

	@Test
	void GsonTest() {
		Hall sala = creaSala(30, State.INUSO, State.PRENOTATA);
		String eventJson = gson.toJson(sala);
		Hall deserializedHall = gson.fromJson(eventJson, Hall.class);
		assertEquals(sala, deserializedHall);
	}

	@Test
	void loadFromDbTest() {
		sale = hallService.getRisorse();
		assertFalse(sale.isEmpty());
		for (Hall sala : sale) {
			assertNotNull(sala.getNome());
			assertFalse(sala.getNome().isEmpty());
		}
	}

    @SuppressWarnings("null")
	@Test
    void getSaleLibereTest(){
		sale = hallService.getRisorse();
		List<String> saleLibere= null;
		boolean found = false;
		LocalDate dataTest=null;
		for (Hall sala : sale) {
			for (StateDate sd : sala.getDisponibilita()) {
				if (sd.getStato() != State.DISPONIBILE) {
					dataTest = sd.getData();
					found = true;
					break;
				}
			}
		}
		if (found) {
			saleLibere = hallService.getSaleLibere(dataTest);
		}
		assertNotEquals(sale.size(),saleLibere.size());
    }
}

