package client.windows;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import server.AppConfig;
import server.model.*;

public class WResourceRest {

    protected final RestTemplate restTemplate;
    protected static final Integer ROOM = 1;
    protected static final Integer HALL = 2;
    protected static final Integer CALENDAR = 3;

    public WResourceRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("null")
    protected boolean cambiaStatoRest(Integer tipo, String name, LocalDate data, State stato) {
        String url;
        if (tipo == ROOM) //NOSONAR
                url = AppConfig.getURL() + "api/room/" + name + "/state";
            else if (tipo == HALL) //NOSONAR
                url = AppConfig.getURL() + "api/hall/" + name + "/state";
            else if (tipo == CALENDAR) //NOSONAR
                url = AppConfig.getURL() + "api/calendar/state";
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
    protected ResponseEntity<server.model.Event> getEventByDateHallRest(LocalDate date, String hallName) {
        String url = AppConfig.getURL() + "api/eventi/" + date.toString() + "/" + hallName;
        return restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            null, 
            new ParameterizedTypeReference<server.model.Event>() {});
    }

}
