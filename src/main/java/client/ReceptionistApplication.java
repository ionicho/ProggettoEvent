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
        primaryStage.setTitle("Ricerca Evento");

        TextField idField = new TextField();
        idField.setPromptText("Inserisci l'ID dell'evento");

        TextField responseField = new TextField();
        responseField.setEditable(false); // Impedisce all'utente di modificare il campo di risposta

        Button searchButton = new Button("Cerca");
        searchButton.setOnAction(e -> searchEvent(idField.getText(), responseField));

        VBox vbox = new VBox(idField, searchButton, responseField);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

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
        Event evento = restTemplate.getForObject(url, Event.class);
        responseField.setText(evento.toString()); // Mostra la risposta del server nel campo di testo
    }
    
    private void searchRoom(String nome, TextField responseField) {
        String url = "http://localhost:8080/api/room/" + nome;
        // Crea un'istanza di Gson registrando il TypeAdapter per LocalDate
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
        // Deserializza la JSON data in un ResourceRoom
        ResourceRoom room = gson.fromJson(restTemplate.getForObject(url, String.class), ResourceRoom.class);
        // Mostra la risposta del server nel campo di testo
        responseField.setText("Nome: " + room.getName() + "\nCosto: " + room.getCosto() + "\nN. letti: " + room.getNLetti() + "\nTipo: " + room.getType());
    }


    public static void main(String[] args) {
        launch(args);
    }
}

