package client.windows;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.List;

import server.AppConfig;
import server.model.*;

/**
 *  Classe che gestisce gli eventi della WRoom
 *  Al click destro sulle celle della disponibilità delle camere fa comparire un menu
 *  contestuale consentendo di scegliere il nuovo stato per la camera.
 *  Invia la richesta al servere (con la classe anonima) e quando riceve l'ok aggiorna
 *  schermata.
 */

public class WResourceRest <T extends Resource> implements EventHandler<MouseEvent> {

    private final RestTemplate restTemplate;
    private final WRoom wRoom; // Riferimento a WRoom1
    private final WHall wHall; // Riferimento a WHall
    private final WCalendar wCalendar; //Riferimento a WCalendar
    private static final Integer ROOM = 1;
    private static final Integer HALL = 2;
    private static final Integer CALENDAR = 3;
    private Integer tipo;
    private final State[] statiPossibili;

    public WResourceRest(WRoom wRoom, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.wRoom = wRoom;
        this.wHall = null;
        this.wCalendar = null;
        this.tipo = ROOM;
        statiPossibili = new State[]{State.INUSO, State.DISPONIBILE, State.PULIZIA, State.PRENOTATA};
    }

    public WResourceRest(WHall wHall, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.wHall = wHall;
        this.wRoom = null;
        this.wCalendar = null;
        this.tipo = HALL;
        statiPossibili = new State[]{State.INUSO, State.DISPONIBILE, State.PULIZIA, State.PRENOTATA};
    }

    public WResourceRest(WCalendar wCalendar, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.wCalendar = wCalendar;
        this.wRoom = null;
        this.wHall = null;
        this.tipo = CALENDAR;
        statiPossibili = new State[]{State.CHIUSO, State.DISPONIBILE};
    }

    public TableCell<T, String> creaCellaColorate() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                if (item == null || empty) {setStyle(""); return;}
                switch (item) {
                    case "DISPONIBILE":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #90ee90"); // Verde chiaro
                        break;
                    case "PRENOTATA":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #ffff99"); // Giallo chiaro
                        break;
                    case "INUSO":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #add8e6"); // Blu chiaro
                        break;
                    case "PULIZIA":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #d3d3d3"); // Grigio chiaro
                        break;
                    case "CHIUSO":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #ff7f7f"); // Rosso chiaro
                        break;
                    default:
                        setStyle("-fx-alignment: CENTER");
                        break;
                }
                if (item == "CHIUSO" && (tipo ==ROOM || tipo == HALL)) //NOSONAR
                    setOnMouseClicked(null);
                else if(item == "PRENOTATA" && tipo == HALL ) //NOSONAR
                    setOnMouseClicked(new WResourceRest<>(wHall, restTemplate));
                else if (tipo == ROOM) //NOSONAR
                    setOnMouseClicked(new WResourceRest<>(wRoom, restTemplate));
                else if (tipo == HALL) //NOSONAR
                    setOnMouseClicked(new WResourceRest<>(wHall, restTemplate));
                else if (tipo == CALENDAR) //NOSONAR
                    setOnMouseClicked(new WResourceRest<>(wCalendar, restTemplate));
                else
                    setOnMouseClicked(null);
            }
        };
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && tipo == HALL) { //NOSONAR
            System.out.println("CLICK SX");
            TableCell <T, State> cell = (TableCell) event.getSource();
            T resource = cell.getTableRow().getItem();
            String hallName = resource.getNome();
            LocalDate date = LocalDate.parse(cell.getTableColumn().getText());
            getEventByDateHall(date, hallName);
        }
        if (event.getButton() == MouseButton.SECONDARY && tipo != HALL) { //NOSONAR
           TableCell <T, State >cell = (TableCell) event.getSource();
            T resource = cell.getTableRow().getItem();
            String resourceName = resource.getNome();
            LocalDate data = LocalDate.parse(cell.getTableColumn().getText());
            ContextMenu menuStati = new ContextMenu();
            for (State stato : statiPossibili) {
                MenuItem item = new MenuItem(stato.name());
                item.setOnAction(cambiaStato(resourceName, data, stato));
                menuStati.getItems().add(item);
            }
            menuStati.show((javafx.scene.Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }
    
    @SuppressWarnings("null")
    private void getEventByDateHall(LocalDate date, String hallName) {
        String url = AppConfig.getURL() + "api/eventi/" + date.toString() + "/" + hallName;
        ResponseEntity<Event> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Event>() {});
        if (response.getStatusCode() == HttpStatus.OK) {
            Event event = response.getBody();
            WEvent wEvent = wHall.getWEvent();
            if (wEvent != null) {
                wEvent.getEvento(event.getId(), restTemplate);
            }
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("La sala è prenotata per l'evento:");
                alert.setHeaderText(null);
                alert.setContentText(event.toString());
                alert.showAndWait();
            });
        }
    }

    @SuppressWarnings("null")
    private EventHandler<ActionEvent> cambiaStato(String name, LocalDate data, State stato) {
        return e -> {
            String url;
            if (tipo == ROOM) //NOSONAR
                url = AppConfig.getURL() + "api/room/" + name + "/state";
            else if (tipo == HALL) //NOSONAR
                url = AppConfig.getURL() + "api/hall/" + name + "/state";
            else if (tipo == CALENDAR) //NOSONAR
                url = AppConfig.getURL() + "api/calendar/state";
            else //ERRORE
                return;
            if (restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<>(new StateDate(data, stato)),
                    Void.class
                ).getStatusCode() == HttpStatus.OK) {
                // Aggiorna la GUI qui
                if (tipo == ROOM) //NOSONAR
                    Platform.runLater(wRoom::aggiornaTabella);
                else if (tipo == HALL) //NOSONAR
                    Platform.runLater(wHall::aggiornaTabella);
                else if (tipo == CALENDAR) //NOSONAR
                    Platform.runLater(wCalendar::mettiDati);
            }
        };
    }



}