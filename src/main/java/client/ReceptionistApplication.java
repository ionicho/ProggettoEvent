package client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

import client.Windows.*;

/**
 * Main del client utilizzato dal Receptionist
 */

public class ReceptionistApplication extends Application {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void start(Stage primaryStage) {
        // finestra per l'evento
        Stage eventStage = new Stage();
        WEvent wEvent = new WEvent();
        wEvent.start(eventStage, restTemplate);

        // finestra per la camera
        Stage roomStage = new Stage();
        WRoom wRoom = new WRoom();
        wRoom.start(roomStage, restTemplate);
        
        // finestra di tutte le camere
        Stage roomStage2 = new Stage();
        WRoom2 wRoom2 = new WRoom2();
        wRoom2.start(roomStage2, restTemplate);
    }
   

    public static void main(String[] args) {
        launch(args);
    }
}

