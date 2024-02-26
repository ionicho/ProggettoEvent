package server;

import java.util.*;
import server.model.*;

/**
 * Interfaccia per i subscriber (elenco service) che devono essere notificati
 * dal CalendarController quando cambia la disponibilità del calendario.
 */

public interface Subscriber {
        List<String> changeDisponibilita(List<StateDate> disponibilita);
}
