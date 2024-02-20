package client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import server.AppConfig;
import client.windows.Menu;

/**
 * Main del client utilizzato dal Receptionist
 */

public class ReceptionistApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Configura RestTemplate con l'adattatore IntegerTypeAdapter
    	 RestTemplate restTemplate = AppConfig.configureRestTemplate();
        
        // Crea la finestra del menu
        Menu menu = new Menu();
        menu.start(primaryStage, restTemplate);
    }   

    public static void main(String[] args) {
        launch(args);
    }
}