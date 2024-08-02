package client.windows;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import server.AppConfig;
import server.model.*;

/**
 * Classe per la gestione delle risorse tramite richieste REST ai vari controller.
 * @param <T> tipo generico che estende {@link server.model.Resource}, rappresenta
 * il tipo di risorsa gestita.
 * @param tipo parametro della classe {@link ResourceType}, utilizzato per 
 * semplificare le condizioni if nel codice.
 */

public class WResourceRest <T extends Resource>{

    private final RestTemplate restTemplate;
    private ResourceType tipo;

    public WResourceRest(WResource<T> wResource, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        if (wResource instanceof WRoom) {
            tipo = ResourceType.ROOM;
        } else if (wResource instanceof WHall) {
            tipo = ResourceType.HALL;
        } else if (wResource instanceof WCalendar) {
            tipo = ResourceType.CALENDAR;
        } else {
            tipo = null;
        }
    }

    @SuppressWarnings("unchecked")
    public T[] getDati() {
        String url;
        if (tipo == ResourceType.ROOM) {
            url = AppConfig.getURL() + "api/room";
            return (T[]) restTemplate.getForObject(url, Room[].class);
        } else if (tipo == ResourceType.HALL) {
            url = AppConfig.getURL() + "api/hall";
            return (T[]) restTemplate.getForObject(url, Hall[].class);
        } else if (tipo == ResourceType.CALENDAR) {
            url = AppConfig.getURL() + "api/calendar";
            return (T[]) restTemplate.getForObject(url, Calendar[].class);
        } else {
            return null; //NOSONAR
        }
    }

    @SuppressWarnings("null")
    public boolean cambiaStatoRest(String name, LocalDate data, State stato) {
        String url;
        if (tipo == ResourceType.ROOM)
                url = AppConfig.getURL() + "api/room/" + name + "/state"; //NOSONAR
            else if (tipo == ResourceType.HALL)
                url = AppConfig.getURL() + "api/hall/" + name + "/state"; //NOSONAR
            else if (tipo == ResourceType.CALENDAR)
                url = AppConfig.getURL() + "api/calendar/" + name + "/state"; //NOSONAR
            else //ERRORE
                return false;
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(new StateDate(data, stato)),
                Void.class
        ).getStatusCode() == HttpStatus.OK;
    }

    @SuppressWarnings("null")
    public ResponseEntity<server.model.Event> getEventByDateHallRest(LocalDate date, String hallName) {
        String url = AppConfig.getURL() + "api/event/" + date.toString() + "/" + hallName;
        return restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            null, 
            new ParameterizedTypeReference<server.model.Event>() {});
    }

}
