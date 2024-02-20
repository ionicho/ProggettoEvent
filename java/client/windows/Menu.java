package client.windows;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.*;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.*;
import java.util.*;
import server.AppConfig;

public class Menu {

    private List<Stage> openStages = new ArrayList<>();

    @SuppressWarnings("null")
    public void start(Stage primaryStage, RestTemplate restTemplate){
        
        primaryStage.initStyle(StageStyle.UNDECORATED); // Rimuove la "x" dalla finestra
        openStages.add(primaryStage);
        Button buttonCalendario = new Button("Gestione Calendario");
        buttonCalendario.setTooltip(new Tooltip("Clicca qui per gestire il calendario"));
        Button buttonCamere = new Button("Gestione Camere");
        buttonCamere.setTooltip(new Tooltip("Clicca qui per gestire le camere"));
        Button buttonEventi = new Button("Gestione Eventi");
        buttonEventi.setTooltip(new Tooltip("Clicca qui per gestire gli eventi"));
        Button buttonArresto = new Button("Arresto del sistema");
        buttonArresto.setTooltip(new Tooltip("Clicca qui per arrestare il sistema"));

        buttonCalendario.setOnAction(e -> {
            Stage calendarStage = new Stage();
            openStages.add(calendarStage);
            WCalendar wCalendar = new WCalendar();
            wCalendar.start(calendarStage, restTemplate);
            primaryStage.hide(); // Nasconde la finestra del menu
            calendarStage.setOnHidden(event -> primaryStage.show()); // Mostra la finestra del menu quando la nuova finestra viene chiusa
        });

        buttonCamere.setOnAction(e -> {
            Stage roomStage = new Stage();
            openStages.add(roomStage);
            WRoom wRoom = new WRoom();
            wRoom.start(roomStage, restTemplate);
            primaryStage.hide(); // Nasconde la finestra del menu
            roomStage.setOnHidden(event -> primaryStage.show()); // Mostra la finestra del menu quando la nuova finestra viene chiusa
        });

        buttonEventi.setOnAction(e -> {
            Stage eventStage = new Stage();
            openStages.add(eventStage);
            WEvent wEvent = new WEvent();
            wEvent.start(eventStage, restTemplate);
            primaryStage.hide(); // Nasconde la finestra del menu
            eventStage.setOnHidden(event -> primaryStage.show()); // Mostra la finestra del menu quando la nuova finestra viene chiusa
        });

        buttonArresto.setOnAction(e -> {
            String url = AppConfig.getURL() + "api/arresta-server";
            try {
                restTemplate.exchange(url,HttpMethod.DELETE, null,Void.class);
            } catch (ResourceAccessException ex) {
                // Il server si è spento prima di poter rispondere
            }
            for (Stage stage : openStages) {stage.close();}//chiude le finestre
            Platform.exit(); // Chiude il client
        });

        GridPane layout = new GridPane();
        layout.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
        layout.setVgap(10);
        layout.setHgap(10);

        layout.add(buttonCalendario, 0, 0);
        layout.add(buttonCamere, 0, 1);
        layout.add(buttonEventi, 0, 2);
        layout.add(buttonArresto, 0, 3);

        for (int i = 0; i < layout.getRowCount(); i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            layout.getRowConstraints().add(row);
        }

        buttonCalendario.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonCamere.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonEventi.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonArresto.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Scene scene = new Scene(layout, 150, 200);
        primaryStage.setTitle("Menu Principale");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}