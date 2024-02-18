package client.windows;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;

import server.AppConfig;
import server.model.*;

/**
 *  Classe che gestisce gli eventi della WRoom2
 *  Al click destro sulle celle della disponibilità delle camere fa comparire un menu
 *  contestuale consentendo di scegliere il nuovo stato per la camera.
 *  Invia la richesta al servere (con la classe anonima) e quando riceve l'ok aggiorna
 *  schermata.
 */

public class WRoom2Event implements EventHandler<MouseEvent> {

    private final RestTemplate restTemplate;
    private final WRoom2 wRoom2; //Riferimento a WRoom2

    public WRoom2Event(WRoom2 wRoom2) {
        this.restTemplate = new RestTemplate();
        this.wRoom2= wRoom2;
    }

    public TableCell<Room, String> creaCellaColorate() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                if (item == null || empty) {
                    setStyle("");
                    return;
                }
                switch (item) {
                    case "DISPONIBILE":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #90ee90"); // Verde chiaro
                        break;
                    case "PRENOTATA":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #ffff99"); // Giallo chiaro
                        break;
                    case "INUSO":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #ff7f7f"); // Rosso chiaro
                        break;
                    case "PULIZIA":
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #d3d3d3"); // Grigio chiaro
                        break;
                    default:
                        setStyle("-fx-alignment: CENTER");
                        break;
                }
                setOnMouseClicked(new WRoom2Event(wRoom2));
            }
        };
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) { //click dx del mouse
           TableCell <Room, State >cell = (TableCell) event.getSource();
            Room room = cell.getTableRow().getItem();
            String camera = room.getNome();
            LocalDate data = LocalDate.parse(cell.getTableColumn().getText());
            ContextMenu menuStati = new ContextMenu();
            for (State stato : State.values()) {
                MenuItem item = new MenuItem(stato.name());
                item.setOnAction(cambiaStato(camera, data, stato));
                menuStati.getItems().add(item);
            }
            menuStati.show((javafx.scene.Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }
    
    @SuppressWarnings("null")
    private EventHandler<ActionEvent> cambiaStato(String camera, LocalDate data, State stato) {
        return e -> {
            if (restTemplate.exchange(
                    AppConfig.getURL() + "api/room/" + camera + "/state",
                    HttpMethod.PUT,
                    new HttpEntity<>(new StateDate(data, stato)),
                    Void.class
                ).getStatusCode() == HttpStatus.OK) {
                // Aggiorna la GUI qui
                Platform.runLater(wRoom2::aggiornaTabella);
            }
        };
    }

}

