package client.Windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import server.model.*;

public class WRoom2eHandler implements EventHandler<MouseEvent> {

    private final RestTemplate restTemplate;
    
    public WRoom2eHandler() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            TableCell cell = (TableCell) event.getSource();
            ResourceRoom room = (ResourceRoom) cell.getTableRow().getItem();
            String camera = room.getNome();
            LocalDate data = LocalDate.parse(cell.getTableColumn().getText());

            ContextMenu contextMenu = new ContextMenu();

            MenuItem disponibile = new MenuItem(State.DISPONIBILE.name());
            disponibile.setOnAction(createMenuItemHandler(camera, data, State.DISPONIBILE));
            contextMenu.getItems().add(disponibile);

            MenuItem impegnato = new MenuItem(State.PRENOTATA.name());
            impegnato.setOnAction(createMenuItemHandler(camera, data, State.PRENOTATA));
            contextMenu.getItems().add(impegnato);

            MenuItem inuso = new MenuItem(State.INUSO.name());
            inuso.setOnAction(createMenuItemHandler(camera, data, State.INUSO));
            contextMenu.getItems().add(inuso);

            MenuItem pulizia = new MenuItem(State.PULIZIA.name());
            pulizia.setOnAction(createMenuItemHandler(camera, data, State.PULIZIA));
            contextMenu.getItems().add(pulizia);

            contextMenu.show((javafx.scene.Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }
    ////////////////// inizio classe anonima /////////////////////////   
    private EventHandler<ActionEvent> createMenuItemHandler(String camera, LocalDate data, State stato) {
		   return new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent e) {
				StateDate statoData = new StateDate(data,stato);
				System.out.println("per la camera " + camera + " selezionato " + statoData);
				HttpEntity<StateDate> request = new HttpEntity<>(statoData);
				restTemplate.exchange("http://localhost:8080/api/room/" + camera, HttpMethod.PATCH, request, Void.class);

			}
		};
    }
    ////////////////// fine classe anonima /////////////////////////     

}

