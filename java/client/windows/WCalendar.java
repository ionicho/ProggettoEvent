package client.windows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.client.RestTemplate;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import server.model.*;

/**
 * Classe per la gestione della finestra del calendario
 * @restTemplate è un'istanza per i servizi RESTful
 * @Table raccoglie i dati di calendar.disponibilita
 */
public class WCalendar {

    private TableView<StateDate> table;
    
    public TableView<StateDate> getTable(){
    	return this.table;
    }

	public void start(Stage primaryStage, RestTemplate restTemplate) {
	    this.table = new TableView<>();
	    WCalendarEvent event = new WCalendarEvent(this, restTemplate);
	    primaryStage.setTitle("Gestione Calendario");
		    	    
	 // Crea le etichette
	    Label startDateL = new Label("Data inizio:");
	    Label endDateL = new Label("Data fine:");
  
	 // Crea i DatePicker e li formatta perchè abbiano lo stesso formato di disponibilita.Date
	    DatePicker startDatePicker = new DatePicker();
	    DatePicker endDatePicker = new DatePicker();
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    startDatePicker.setConverter(new StringConverter<>() {
	        @Override
	        public String toString(LocalDate date) {
	            return (date != null) ? dateFormatter.format(date) : "";	        }        
	        @Override
	        public LocalDate fromString(String string) {
	            return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;	        }
	    });
	    endDatePicker.setConverter(startDatePicker.getConverter()); // Usa lo stesso convertitore per entrambi i DatePicker

	    // Crea il pulsante e gli associa un evento
	    Button setCalendarButton = new Button("Crea Calendario");
	    setCalendarButton.setOnAction(e -> {
	        LocalDate startDate = startDatePicker.getValue();
	        LocalDate endDate = endDatePicker.getValue();
	        event.setCalendario(startDate, endDate);
	    });
 
	    // Crea le colonne per il calendario con gli stati
	    TableColumn<StateDate, String> dataCol = new TableColumn<>("Data");
	    dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
	    table.getColumns().add(dataCol);
	    TableColumn<StateDate, State> statoCol = new TableColumn<>("Stato");
	    statoCol.setCellValueFactory(new PropertyValueFactory<>("stato"));
	    statoCol.setCellFactory(column -> event.coloraCelle());
	    table.getColumns().add(statoCol);

	    // Crea il layout griglia per le etichette e i DatePicker
	    GridPane griglia = new GridPane();
	    griglia.setHgap(10);
	    griglia.setVgap(10);
	    griglia.add(startDateL, 1, 2);
	    griglia.add(startDatePicker, 1, 3);
	    griglia.add(endDateL, 2, 2);
	    griglia.add(endDatePicker, 2, 3);
	    griglia.add(setCalendarButton, 1, 4, 2, 1);
	    griglia.add(table, 1, 5, 2, 10);
	    Pane bordoSotto = new Pane();
	    bordoSotto.setMinHeight(10); //
	    griglia.add(bordoSotto, 1, 15, 2, 1);

	    event.mettiDati();

	    Scene scene = new Scene(griglia, 400, 400);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
   
}
    
    

