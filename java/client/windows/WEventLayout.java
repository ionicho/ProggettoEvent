package client.windows;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import server.model.*;

/**
 * Superclasse di WEvent, contiene i metodi per la definizione
 * dei campi e del layout della finestra.
 */

public class WEventLayout {
	protected static final int WIDTH =150;
    protected static final String ORAMINUTI = "HH:mm";
	protected GridPane grid;
    protected Label cercaL, msgL, idLabel, nomeOrgL, costoParL, numParPreL, cateringL, dataL, oraIniL, oraFinL, salaL, interventiL;
    protected TextField cercaF, idField, msgF, nomeOrgF, costoParF, numParPreF, oraIniF, oraFinF;
    protected ComboBox<String> cateringF, salaF;
	protected DatePicker dataF;
	protected List<Event> eventi;
    protected TableView<Speech> interventiLista;
    protected TableColumn<Speech, String> titolo, relatore, descrizione, elimina;
    protected Button firstButton, prevButton, nextButton, lastButton, newButton, saveButton, newSpeech;

	/** Inizializza i campi */
	protected void setFields(Stage stage) {
        cercaF = creaTextField();
        idField = creaTextField();
        msgF = creaTextField();
        nomeOrgF = creaTextField();
        costoParF = creaTextField();
		numParPreF = creaTextField();
		String[] cateringOptions = Arrays.stream(CateringType.values())
			.map(CateringType::name)
			.toArray(String[]::new);
		cateringF = creaComboBox(cateringOptions);
        oraIniF  = creaHourField();
        oraFinF  = creaHourField();
		salaF = new ComboBox<>();
		salaF.setPrefWidth(WIDTH);
		dataF = new DatePicker();
		dataF.setPrefWidth(WIDTH);
        interventiLista = new TableView<>();
		interventiLista.setEditable(true);
        titolo = new TableColumn<>("Titolo");
		titolo.setCellFactory(TextFieldTableCell.forTableColumn()); //rende modificabile
        relatore = new TableColumn<>("Relatore");
		relatore.setCellFactory(TextFieldTableCell.forTableColumn());
        descrizione = new TableColumn<>("Descrizione");
		descrizione.setCellFactory(TextFieldTableCell.forTableColumn());
		elimina = new TableColumn<>("Azione");
		firstButton = creaButton("Primo Evento");
		prevButton = creaButton("Evento Precedente");
		nextButton = creaButton("Prossimo Evento");
		lastButton = creaButton("Ultimo Evento");
		newButton = creaButton("Crea Nuovo Evento");
        saveButton = creaButton("Salva / Modifica Evento");
        newSpeech = creaButton("Crea nuovo Intervento");
        cercaL = creaLabel("ID da cercare:");
        msgL = creaLabel("Messaggio:");
        idLabel = creaLabel("ID evento:");
        nomeOrgL = creaLabel("Nome Organizzatore:");
        costoParL = creaLabel("Costo Partecipazione:");
		numParPreL = creaLabel("Partecipanti Previsti:");
		cateringL = creaLabel("Catering:");
        dataL = creaLabel("Data:");
        oraIniL = creaLabel("Ora Inizio:");
        oraFinL = creaLabel("Ora Fine:");
		salaL = creaLabel("Sala:");
        interventiL = creaLabel("Elenco Interventi:");
    }
	
	/** Imposta il layout */
    protected void setLayout() {
		int row=0;
		grid = new GridPane();
		grid.setHgap(.10 * WIDTH);
		grid.setVgap(.10 * WIDTH);
		grid.setPrefWidth(6.2* WIDTH);
		grid.setPadding(new Insets(.10 * WIDTH, .10 * WIDTH, .10 * WIDTH, .10 * WIDTH));  // Imposta un margine su tutti i lati
		idField.setEditable(false); 
		msgF.setEditable(false); // Impedisce all'utente di modificare il campo di risposta
		dataF.setPrefWidth(WIDTH);
		titolo.setPrefWidth(WIDTH); 
		relatore.setPrefWidth(WIDTH);
		descrizione.setPrefWidth(3.55*WIDTH);
		elimina.setPrefWidth(.42*WIDTH);
		elimina.setSortable(false);
        grid.add(cercaL,0,row);
        grid.add(cercaF, 1, row);
        grid.add(msgL, 0, ++row);
        grid.add(msgF, 1,row);
        grid.add(idLabel, 0, ++row);
        grid.add(idField, 1, row);
        grid.add(nomeOrgL, 0, ++row);
        grid.add(nomeOrgF, 1, row);
        grid.add(costoParL, 0, ++row);
        grid.add(costoParF, 1, row);
		grid.add(numParPreL, 0, ++row);
		grid.add(numParPreF, 1, row);
		grid.add(cateringL, 0, ++row);
		grid.add(cateringF, 1, row);
        grid.add(dataL, 0, ++row);
        grid.add(dataF, 1, row);
		grid.add(salaL, 0, ++row);
		grid.add(salaF, 1, row);
        grid.add(oraIniL, 0, ++row);
        grid.add(oraIniF, 1, row);
        grid.add(oraFinL, 0, ++row);
        grid.add(oraFinF, 1, row);
        grid.add(interventiL, 0, ++row);
		grid.add(newSpeech,1,row);
		interventiLista.getColumns().add(titolo);
		interventiLista.getColumns().add(relatore);
		interventiLista.getColumns().add(descrizione);
		interventiLista.getColumns().add(elimina);
		grid.add(interventiLista,0,++row,6,1);
		GridPane.setHalignment(interventiLista, HPos.CENTER);
		interventiLista.setPrefHeight(200);
		grid.add(firstButton, 0, ++row);
		grid.add(prevButton, 1, row);
		grid.add(nextButton, 2, row);
		grid.add(lastButton, 3, row);	
		grid.add(newButton, 4, row);
        grid.add(saveButton, 5, row);
    }
		
	public Button creaButton (String text) {
		Button button = new Button(text);
        GridPane.setHalignment(button, HPos.CENTER);
        button.setPrefWidth(WIDTH);
        return button;
	}
    
    public Label creaLabel(String text) {
        Label label = new Label(text);
        GridPane.setHgrow(label, Priority.ALWAYS);
        GridPane.setHalignment(label, HPos.RIGHT);
        label.setPrefWidth(WIDTH);
        return label;
    }
    
    public TextField creaTextField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        field.setText("");
        return field;
    }

    public ComboBox<String> creaComboBox(String[] options) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(WIDTH);
        comboBox.getItems().addAll(options);
        return comboBox;
    }
    
    public TextField creaIntegerField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        return field;
    }
    
    public TextField creaDoubleField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        field.setText("0.0");
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldValue);
            }
        });
        return field;
    }
    
    public TextField creaHourField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        field.setText("");
        field.setPromptText("HH:mm");
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("([01]?\\d|2[0-3])?(:[0-5]?\\d?)?") || newValue.length() > 5) {
                field.setText(oldValue);
            }
        });
        return field;
    }

}