package client.windows;

import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import server.model.*;

/**
 *  Classe che gestisce gli eventi della WRoom
 *  Al click destro sulle celle della disponibilità delle camere fa comparire un menu
 *  contestuale consentendo di scegliere il nuovo stato per la camera.
 *  Invia la richesta al servere (con la classe anonima) e quando riceve l'ok aggiorna
 *  schermata.
 */

public class WResourceClick <T extends Resource> extends WResourceRest implements EventHandler<MouseEvent> {


    private final WRoom wRoom; // Riferimento a WRoom1
    private final WHall wHall; // Riferimento a WHall
    private final WCalendar wCalendar; //Riferimento a WCalendar

    private Integer tipo;
    private final State[] statiPossibili;

    public WResourceClick(WRoom wRoom, RestTemplate restTemplate) {
        super(restTemplate);
        this.wRoom = wRoom;
        this.wHall = null;
        this.wCalendar = null;
        this.tipo = ROOM;
        statiPossibili = new State[]{State.INUSO, State.DISPONIBILE, State.PULIZIA, State.PRENOTATA};
    }

    public WResourceClick(WHall wHall, RestTemplate restTemplate) {
        super(restTemplate);
        this.wHall = wHall;
        this.wRoom = null;
        this.wCalendar = null;
        this.tipo = HALL;
        statiPossibili = new State[]{State.INUSO, State.DISPONIBILE, State.PULIZIA, State.PRENOTATA};
    }

    public WResourceClick(WCalendar wCalendar, RestTemplate restTemplate) {
        super(restTemplate);
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
                    setOnMouseClicked(new WResourceClick<>(wHall, restTemplate));
                else if (tipo == ROOM) //NOSONAR
                    setOnMouseClicked(new WResourceClick<>(wRoom, restTemplate));
                else if (tipo == HALL) //NOSONAR
                    setOnMouseClicked(new WResourceClick<>(wHall, restTemplate));
                else if (tipo == CALENDAR) //NOSONAR
                    setOnMouseClicked(new WResourceClick<>(wCalendar, restTemplate));
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
        ResponseEntity<server.model.Event> response = getEventByDateHallRest(date, hallName);  
        if (response.getStatusCode() == HttpStatus.OK) {
            server.model.Event event = response.getBody();
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
        }    }
         
    private EventHandler<ActionEvent> cambiaStato(String name, LocalDate data, State stato) {
        return e -> {
            if (cambiaStatoRest(tipo, name, data, stato)) {
                if (tipo == ROOM) //NOSONAR
                    Platform.runLater(wRoom::refresh);
                else if (tipo == HALL) //NOSONAR
                    Platform.runLater(wHall::refresh);
                else if (tipo == CALENDAR) //NOSONAR
                    Platform.runLater(wCalendar::mettiDati);
            }
        };
    }

}