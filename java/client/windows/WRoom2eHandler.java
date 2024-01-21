package client.windows;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;

import server.model.*;

/**
 *  Classe che gestisce gli eventi della WRoom2
 *  Al click destro sulle celle della disponibilità delle camere fa comparire un menu
 *  contestuale consentendo di scegliere il nuovo stato per la camera.
 *  Invia la richesta al servere (con la classe anonima) e quando riceve l'ok aggiorna
 *  schermata.
 */

public class WRoom2eHandler implements EventHandler<MouseEvent> {

    private final RestTemplate restTemplate;
    private final WRoom2 wRoom2; //Riferimento a WRoom2

    public WRoom2eHandler(WRoom2 wRoom2) {
        this.restTemplate = new RestTemplate();
        this.wRoom2= wRoom2;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) { //click dx del mouse
           TableCell <ResourceRoom, State >cell = (TableCell) event.getSource();
            ResourceRoom room = cell.getTableRow().getItem();
            String camera = room.getNome();
            LocalDate data = LocalDate.parse(cell.getTableColumn().getText());
            ContextMenu menuStati = new ContextMenu();
            for (State stato : State.values()) {
                MenuItem item = new MenuItem(stato.name());
                item.setOnAction(createMenuItemHandler(camera, data, stato));
                menuStati.getItems().add(item);
            }
            menuStati.show((javafx.scene.Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }
    
      
	//////////////////inizio classe anonima /////////////////////////   
    private EventHandler<ActionEvent> createMenuItemHandler(String camera, LocalDate data, State stato) {
        return new EventHandler<ActionEvent>() {
        
            @Override
            public void handle(ActionEvent e) {
                StateDate statoData = new StateDate(data,stato);
                System.out.println("per la camera " + camera + " selezionato " + statoData);
                HttpEntity<StateDate> request = new HttpEntity<>(statoData);
                ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8080/api/room/" + camera + "/state", HttpMethod.PUT, request, Void.class);
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    // Aggiorna la GUI qui
                	    Platform.runLater(() -> Platform.runLater(wRoom2::aggiornaTabella));
                }
            }
        };
    }
	//////////////////fine classe anonima /////////////////////////

  

}

