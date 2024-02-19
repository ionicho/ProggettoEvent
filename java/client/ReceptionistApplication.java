package client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import server.AppConfig;
import client.windows.*;

/**
 * Main del client utilizzato dal Receptionist
 */

public class ReceptionistApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Configura RestTemplate con l'adattatore IntegerTypeAdapter
    	 RestTemplate restTemplate = AppConfig.configureRestTemplate();
        
        // finestra per il calendario
        Stage calendarStage = new Stage();
        WCalendar wCalendar = new WCalendar();
        wCalendar.start(calendarStage, restTemplate);
        
        /* finestra per l'evento
        Stage eventStage = new Stage();
        WEvent wEvent = new WEvent();
        wEvent.start(eventStage, restTemplate);
 */
        // finestra per la camera
        Stage roomStage = new Stage();
        WRoom1 wRoom1 = new WRoom1();
        wRoom1.start(roomStage, restTemplate);
        
        // finestra di tutte le camere
        Stage roomStage2 = new Stage();
        WRoom wRoom = new WRoom(); 
        wRoom.start(roomStage2, restTemplate);

                // finestra di tutte le sale
                Stage hallStage = new Stage();
                WHall wHall = new WHall(); 
                wHall.start(hallStage, restTemplate);
    }   

    public static void main(String[] args) {
        launch(args);
    }
}