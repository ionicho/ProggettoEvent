package server;

import java.util.*;
import server.model.*;

public interface Subscriber {
        List<String> updateState(List<StateDate> disponibilita);
}
