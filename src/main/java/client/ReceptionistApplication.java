package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import server.model.Event;

import org.springframework.web.client.RestTemplate;

/**
 * Main del client utilizzato dal Receptionist
 */

public class ReceptionistApplication extends Application {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void start(Stage primaryStage) {
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
    }

    private void searchEvent(String id, TextField responseField) {
        String url = "http://localhost:8080/api/eventi/" + id;
        Event evento = restTemplate.getForObject(url, Event.class);
        responseField.setText(evento.toString()); // Mostra la risposta del server nel campo di testo
    }

    public static void main(String[] args) {
        launch(args);
    }
}

