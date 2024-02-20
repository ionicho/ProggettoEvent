package client.windows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.web.client.RestTemplate;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import server.AppConfig;
import server.model.*;

/**
 * Classe per la gestione della finestra del calendario
 * @restTemplate è un'istanza per i servizi RESTful
 * @Table raccoglie i dati di calendar.disponibilita
 */
public class WCalendar extends WResource<Calendar> {
	private Scene scene;
	private GridPane griglia;
    
	public WCalendar(RestTemplate restTemplate) {
        super(restTemplate);
        this.url = AppConfig.getURL() + "api/calendar";
        this.stdHandler = new StdHandler<>(this, restTemplate);
        addColonneStatiche();
		addContolli();
        mettiDati();
		TableColumn<Calendar, ?> nomeColumn = table.getColumns().stream()
			.filter(c -> "Nome".equals(c.getText()))
			.findAny()
			.orElse(null);
		TableColumn<Calendar, ?> costoColumn = table.getColumns().stream()
			.filter(c -> "Costo".equals(c.getText()))
			.findAny()
			.orElse(null);
		if (nomeColumn != null) {nomeColumn.setVisible(false);}
		if (costoColumn != null) {costoColumn.setVisible(false);}
        this.scene = new Scene(griglia, 1200, 400);
    }

	public void start(String title, Stage primaryStage) {
		primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	@SuppressWarnings("null")
	@Override
	protected Calendar[] getDati() {
		RestTemplate restTemplate = new RestTemplate();
		Calendar calendar = restTemplate.getForObject(url, Calendar.class);
		return new Calendar[]{calendar}; // Restituisci un array con un singolo elemento
	}

	private void addContolli(){	
	    griglia = new GridPane();
		Label startDateL = new Label("Data inizio:");
	    Label endDateL = new Label("Data fine:");
	    DatePicker startDatePicker = new DatePicker();
	    DatePicker endDatePicker = new DatePicker();
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// Usa lo stesso convertitore per entrambi i DatePicker
	    startDatePicker.setConverter(new StringConverter<>() {
	        @Override
	        public String toString(LocalDate date) {
	            return (date != null) ? dateFormatter.format(date) : "";	        }        
	        @Override
	        public LocalDate fromString(String string) {
	            return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;	        }
	    });
	    endDatePicker.setConverter(startDatePicker.getConverter()); 
	    // Crea il pulsante e gli associa un evento
	    Button setCalendarButton = new Button("Crea Calendario");
	    setCalendarButton.setOnAction(e -> {
	        LocalDate startDate = startDatePicker.getValue();
	        LocalDate endDate = endDatePicker.getValue();
	        stdHandler.setCalendario(startDate, endDate);
	    });
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
	}
}
    
    
