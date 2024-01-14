package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import server.model.*;
import server.service.LocalDateTypeAdapter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.*;


/**
 * Main del client utilizzato dal Receptionist
 */

public class ReceptionistApplication extends Application {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void start(Stage primaryStage) {
    	
    	// finestra per l'evento
        Stage eventStage = new Stage();
        eventStage.setTitle("Ricerca Evento");

        TextField eventIdField = new TextField();
        eventIdField.setPromptText("Inserisci l'ID dell'evento");

        TextField eventResponseField = new TextField();
        eventResponseField.setEditable(false); // Impedisce all'utente di modificare il campo di risposta

        Button eventSearchButton = new Button("Cerca");
        eventSearchButton.setOnAction(e -> searchEvent(eventIdField.getText(), eventResponseField));

        VBox eventVbox = new VBox(eventIdField, eventSearchButton, eventResponseField);
        eventVbox.setPadding(new Insets(10));
        eventVbox.setSpacing(8);

        Scene eventScene = new Scene(eventVbox, 300, 200);
        eventStage.setScene(eventScene);
        eventStage.show();

        // finestra per la camera

        Stage roomStage = new Stage();
        roomStage.setTitle("Ricerca Camera");

        TextField roomIdField = new TextField();
        roomIdField.setPromptText("Inserisci l'ID della camera");

        TextField roomResponseField = new TextField();
        roomResponseField.setEditable(false);

        Button roomSearchButton = new Button("Cerca");
        roomSearchButton.setOnAction(e -> searchRoom(roomIdField.getText(), roomResponseField));

        VBox roomVbox = new VBox(roomIdField, roomSearchButton, roomResponseField);
        roomVbox.setPadding(new Insets(10));
        roomVbox.setSpacing(8);

        Scene roomScene = new Scene(roomVbox, 300, 200);
        roomStage.setScene(roomScene);
        roomStage.show();
    }
    private void searchEvent(String id, TextField responseField) {
        String url = "http://localhost:8080/api/eventi/" + id;
        System.out.printf("\n\n%s\n\n", url);
        Event evento = restTemplate.getForObject(url, Event.class);
        responseField.setText(evento.toString()); // Mostra la risposta del server nel campo di testo
    }
    
    private void searchRoom(String nome, TextField responseField) {
        String url = "http://localhost:8080/api/room/" + nome;
        System.out.printf("\n\n%s\n\n", url);
        ResourceRoom room = restTemplate.getForObject(url, ResourceRoom.class);
        //Object room = restTemplate.getForObject(url, Object.class);
        
        System.out.printf("\n\n%s\n\n", room.toString());
        
        responseField.setText(room.toString()); // Mostra la risposta del server nel campo di testo

        }
   

    public static void main(String[] args) {
        launch(args);
    }
}

