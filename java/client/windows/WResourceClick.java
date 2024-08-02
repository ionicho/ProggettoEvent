package client.windows;

import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import org.springframework.http.*;
import java.time.LocalDate;
import server.model.*;

/**
* Classe per la gestione degli eventi di click sulle celle della 
* tabella gestita da {@link WResource}.
* Qualora serva, la classe utilizza il parametro {@link wResource} per
* effettuare richieste REST al server con l'attributo {@link WResource#restHandler}.
*/

public class WResourceClick <T extends Resource> implements EventHandler<MouseEvent> {

    private WResource <T> wResource;
    private ResourceType tipo;
    private final State[] statiPossibili;

    public WResourceClick(WResource<T> wResource) {
        this.wResource = wResource;
        if (wResource instanceof WRoom) {
            tipo = ResourceType.ROOM;
            statiPossibili = new State[]{State.INUSO, State.DISPONIBILE, State.PULIZIA, State.PRENOTATA};
        } else if (wResource instanceof WHall) {
            tipo = ResourceType.HALL;
            statiPossibili = new State[]{State.INUSO, State.DISPONIBILE, State.PULIZIA, State.PRENOTATA};
        } else if (wResource instanceof WCalendar) {
            tipo = ResourceType.CALENDAR;
            statiPossibili = new State[]{State.CHIUSO, State.DISPONIBILE};
        } else {
            tipo = null;
            statiPossibili = null;
        }
    }

    public void configuraClick(TableColumn<T, String> column) {
        column.setCellFactory(col -> creaCellaColorate());
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
                if (item == "CHIUSO" && (tipo ==ResourceType.ROOM || tipo == ResourceType.HALL)) //NOSONAR
                    setOnMouseClicked(null);
                else if(item.compareTo("PRENOTATA")==0 && tipo == ResourceType.HALL ) 
                    setOnMouseClicked(new WResourceClick<>(wResource));
                else if (tipo == ResourceType.ROOM)
                    setOnMouseClicked(new WResourceClick<>(wResource));
                else if (tipo == ResourceType.HALL)
                    setOnMouseClicked(new WResourceClick<>(wResource));
                else if (tipo == ResourceType.CALENDAR)
                    setOnMouseClicked(new WResourceClick<>(wResource));
                else
                    setOnMouseClicked(null);
            }
        };
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && tipo == ResourceType.HALL) { //NOSONAR
            System.out.println("CLICK SX");
            TableCell <T, State> cell = (TableCell) event.getSource();
            T resource = cell.getTableRow().getItem();
            String hallName = resource.getNome();
            LocalDate date = LocalDate.parse(cell.getTableColumn().getText());
            getEventByDateHall(date, hallName);
        }
        if (event.getButton() == MouseButton.SECONDARY && tipo != ResourceType.HALL) { //NOSONAR
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
        ResponseEntity<server.model.Event> response = wResource.getRestHandler().getEventByDateHallRest(date, hallName);
        if (response.getStatusCode() == HttpStatus.OK) {
                server.model.Event event = response.getBody();
                WEvent wEvent = wResource.getWEvent();
            if (wEvent != null) {
                wEvent.getEvento(event.getNome(), wEvent.getRestTemplate());
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
         
    private EventHandler<ActionEvent> cambiaStato(String name, LocalDate data, State stato) {
        return e -> {
            if (wResource.getRestHandler().cambiaStatoRest(name, data, stato)) {
                if (tipo == ResourceType.ROOM) //NOSONAR
                    Platform.runLater(wResource::refresh);
                else if (tipo == ResourceType.HALL) //NOSONAR
                    Platform.runLater(wResource::refresh);
                else if (tipo == ResourceType.CALENDAR) //NOSONAR
                    Platform.runLater(wResource::mettiDati);
            }
        };

    }
}