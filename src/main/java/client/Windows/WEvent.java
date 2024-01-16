package client.Windows;

import org.springframework.web.client.RestTemplate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import server.model.*;

public class WEvent {

 public void start(Stage eventStage, RestTemplate restTemplate) {
     eventStage.setTitle("Ricerca Evento");

     TextField eventIdField = new TextField();
     eventIdField.setPromptText("Inserisci l'ID dell'evento");

     TextField eventResponseField = new TextField();
     eventResponseField.setEditable(false); // Impedisce all'utente di modificare il campo di risposta

     Button eventSearchButton = new Button("Cerca");
     eventSearchButton.setOnAction(e -> searchEvent(eventIdField.getText(), eventResponseField, restTemplate));

     VBox eventVbox = new VBox(eventIdField, eventSearchButton, eventResponseField);
     eventVbox.setPadding(new Insets(10));
     eventVbox.setSpacing(8);

     Scene eventScene = new Scene(eventVbox, 300, 200);
     eventStage.setScene(eventScene);
     eventStage.show();
 }

 private void searchEvent(String id, TextField responseField, RestTemplate restTemplate) {
     String url = "http://localhost:8080/api/eventi/" + id;
     System.out.printf("\n\n%s\n\n", url);
     Event evento = restTemplate.getForObject(url, Event.class);
     responseField.setText(evento.toString()); // Mostra la risposta del server nel campo di testo
 }
}
