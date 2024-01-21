package client.windows;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import org.springframework.web.client.RestTemplate;

import server.model.*;

public class WRoom {

 public void start(Stage roomStage, RestTemplate restTemplate) {
     roomStage.setTitle("Ricerca Camera");

     TextField roomIdField = new TextField();
     roomIdField.setPromptText("Inserisci l'ID della camera");

     TextField roomResponseField = new TextField();
     roomResponseField.setEditable(false);

     Button roomSearchButton = new Button("Cerca");
     roomSearchButton.setOnAction(e -> searchRoom(roomIdField.getText(), roomResponseField, restTemplate));

     VBox roomVbox = new VBox(roomIdField, roomSearchButton, roomResponseField);
     roomVbox.setPadding(new Insets(10));
     roomVbox.setSpacing(8);

     Scene roomScene = new Scene(roomVbox, 300, 200);
     roomStage.setScene(roomScene);
     roomStage.show();
 }

 private void searchRoom(String nome, TextField responseField, RestTemplate restTemplate) {
	    String url = "http://localhost:8080/api/room/" + nome;
	    System.out.printf("\n\n%s\n\n", url);
	    ResourceRoom room = restTemplate.getForObject(url, ResourceRoom.class);
	    if (room != null) {
	        responseField.setText(room.toString()); // Mostra la risposta del server nel campo di testo
	    } else {
	        responseField.setText("Stanza non trovata"); // Mostra un messaggio di errore
	    }
	}


}
