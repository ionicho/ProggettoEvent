package client.windows;

import org.springframework.web.client.RestTemplate;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import server.model.*;

public class WEvent {

 public void start(Stage eventStage, RestTemplate restTemplate) {
     eventStage.setTitle("Gestione Eventi");
     
     /////////////// per la ricerca del singolo evento /////////////////
     TextField eventResponseField = new TextField();
     eventResponseField.setEditable(false); // Impedisce all'utente di modificare il campo di risposta
     TextField eventIdField = new TextField();
     eventIdField.setPromptText("Inserisci l'ID dell'evento");
     eventIdField.setOnAction(e -> cercaEvento(eventIdField.getText(), eventResponseField, restTemplate));
     
     /////////////// per la creazione di un nuovo evento /////////////////
     Button newEventButton = new Button("Nuovo Evento");
     String url = "http://localhost:8080/api/eventi/nuovo";
     newEventButton.setOnAction(e -> {
         Event nuovoEvento = new Event(); // Crea un nuovo oggetto Evento vuoto
         // Invia la richiesta POST al server
         System.out.printf("%s \n", nuovoEvento.toString());
         Event res =restTemplate.postForObject(url, nuovoEvento, Event.class);
         System.out.printf("res= %s \n", res.toString());
         
         WEvent2 wEvent2 = new WEvent2();
         wEvent2.openWindow(nuovoEvento); // Apri la nuova finestra WEvent2
     });

     /////////////// gestione finestra /////////////////

     VBox eventVbox = new VBox(eventIdField, eventResponseField, newEventButton);
     eventVbox.setPadding(new Insets(10));
     eventVbox.setSpacing(8);
     Scene eventScene = new Scene(eventVbox, 300, 200);
     eventStage.setScene(eventScene);
     eventStage.show();
 }

 private void cercaEvento(String id, TextField responseField, RestTemplate restTemplate) {
	    String url = "http://localhost:8080/api/eventi/" + id;
	    System.out.printf("\n\n%s\n\n", url);
	    Event evento = restTemplate.getForObject(url, Event.class);
	    if (evento != null) {
	        responseField.setText(evento.toString()); // Mostra la risposta del server nel campo di testo
	    } else {
	        responseField.setText("Evento non trovato"); // Mostra un messaggio di errore
	    }
	}
 
}
