package client.windows;

import java.time.LocalTime;

import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import server.model.*;

public class WEvent2 {

    private Stage window;
    private TextField idField;
    private Event evento;

    private GridPane grid;  // Aggiungi un campo per la griglia

    public WEvent2() {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Inserisci Evento");
        grid = createGrid();
        Scene scene = new Scene(grid);
        window.setScene(scene);        
    }
    
    private GridPane createGrid() {
    
        // Crea un layout GridPane per la finestra
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        // Crea i campi
        idField = createTextField();
        idField.setEditable(false); 
        TextField nomeOrgF = createTextField();
        TextField costoParF = createDoubleField();
        TextField oraIniF = createHourField();
        TextField oraFinF = createHourField();
        DatePicker dataF = new DatePicker();
        dataF.setPrefWidth(200);
        TextField interventiF = new TextField(); //ListView<>();

        // Crea le etichette per i campi
        Label idLabel = createLabel("ID:");
        Label nomeOrgL = createLabel("Nome Organizzatore:");
        Label costoParL = createLabel("Costo Partecipazione:");
        Label dataL = createLabel("Data:");
        Label oraIniL = createLabel("Ora Inizio:");
        Label oraFinL = createLabel("Ora Fine:");
        Label interventiL = createLabel("Elenco Interventi:");
        
        // Aggiungi le etichette e i campi al layout della griglia
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(nomeOrgL, 0, 1);
        grid.add(nomeOrgF, 1, 1);
        grid.add(costoParL, 0, 2);
        grid.add(costoParF, 1, 2);
        grid.add(dataL, 0, 3);
        grid.add(dataF, 1, 3);
        grid.add(oraIniL, 0, 4);
        grid.add(oraIniF, 1, 4);
        grid.add(oraFinL, 0, 5);
        grid.add(oraFinF, 1, 5);
        grid.add(interventiL, 0, 6);
        grid.add(interventiF, 1, 6);

        Button saveButton = new Button("Salva");
        saveButton.setOnAction(e -> {
            evento.setNomeOrg(nomeOrgF.getText());
            evento.setCostoPar(Integer.parseInt(costoParF.getText()));
            evento.setData(dataF.getValue());
            evento.setOraInizio(LocalTime.parse(oraIniF.getText()));
            evento.setOraFine(LocalTime.parse(oraFinF.getText()));
            addEvento(evento);
        });

        grid.add(saveButton, 1, 7);
        return grid;
    }
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        GridPane.setHgrow(label, Priority.ALWAYS);
        GridPane.setHalignment(label, HPos.RIGHT);
        return label;
    }
    
    private TextField createTextField() {
        TextField field = new TextField();
        field.setPrefWidth(200);
        return field;
    }
    
    private TextField createDoubleField() {
        TextField field = new TextField();
        field.setPrefWidth(200);
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldValue);
            }
        });
        return field;
    }
    
    private TextField createHourField() {
        TextField field = new TextField();
        field.setPrefWidth(200);
        field.setPromptText("HH:mm");
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("([01]?\\d|2[0-3])?(:[0-5]?\\d?)?") || newValue.length() > 5) {
                field.setText(oldValue);
            }
        });
        return field;
    }

    public void openWindow(Event evento) {
        window.setWidth(400);
        window.setHeight(600);
        this.evento = evento;
        String emptyLabel = "           ";
        GridPane newGrid = createGrid();
        idField.setText(evento.getId());
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(newGrid);
        borderPane.setTop(createLabel(emptyLabel));
        borderPane.setRight(createLabel(emptyLabel));
        borderPane.setBottom(createLabel(emptyLabel));
        borderPane.setLeft(createLabel(emptyLabel));
        
        Scene scene = new Scene(borderPane);
        window.setScene(scene);

        // Mostra la finestra e attendi che venga chiusa
        window.showAndWait();
    }


    private void addEvento(Event evento) {
        // Implementa questo metodo per inviare l'oggetto Event al server
    }
}

