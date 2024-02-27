package client.windows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import server.AppConfig;
import server.model.*;

/**
 * Classe per la gestione della finestra di visualizzazione del calendario.
 * La classe estende {@link WResource} e ne implementa i metodi astratti.
 * addContolli() aggiunge i controlli per la selezione delle date di inizio e fine
 * setCalendario() invia una richiesta PUT al server per aggiornare il calendario
 */

public class WCalendar extends WResource<Calendar> {
	private Scene scene;
	private GridPane griglia;
	LocalDate maxDate = AppConfig.START_DATE;
    
	public WCalendar(RestTemplate restTemplate) {
        super(restTemplate);

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
        this.scene = new Scene(griglia, 1200, 270);
    }

	public void start(String title, Stage primaryStage) {
		primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	private void addContolli(){
	    griglia = new GridPane();
		Label startDateL = new Label("Data inizio:");
	    Label endDateL = new Label("Data fine:");
	    DatePicker startDatePicker = new DatePicker();
		startDatePicker.setValue(AppConfig.START_DATE); // Imposta il valore di default a 01/01/2024
		startDatePicker.setDisable(true); // Blocca il DatePicker
		startDatePicker.setStyle("-fx-opacity: 1.0;"); // Rende il DatePicker visibile
		// Ottieni la data massima dal caledar
		for (Calendar c : getDati()) {
			if (c.getNome().equals("Calendar001")) {
				maxDate = c.getLastDate();
				break;
			}
		}
		DatePicker endDatePicker = new DatePicker();
		endDatePicker.setValue(maxDate);
		// Impedisce la selezione di date precedenti alla data massima
		endDatePicker.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.compareTo(maxDate) < 0);
			}
		});
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
	    Button setCalendarButton = new Button("Estendi il Calendario");
		setCalendarButton.setTooltip(new Tooltip("Modifica il calendario della struttura"));
	    setCalendarButton.setOnAction(e -> {
	        LocalDate startDate = startDatePicker.getValue();
	        LocalDate endDate = endDatePicker.getValue();
	        setCalendario(startDate, endDate);
	    });
	    griglia.setHgap(10);
	    griglia.setVgap(10);
	    griglia.add(startDateL, 1, 2);
	    griglia.add(startDatePicker, 1, 3);
	    griglia.add(endDateL, 2, 2);
	    griglia.add(endDatePicker, 2, 3);
	    griglia.add(setCalendarButton, 4, 3, 2, 1);
	    griglia.add(table, 1, 5, 10, 1);
	    Pane bordoSotto = new Pane();
	    bordoSotto.setMinHeight(10); //
	    griglia.add(bordoSotto, 1, 15, 2, 1);
	}
	
    @SuppressWarnings("null")
    public void setCalendario(LocalDate startDate, LocalDate endDate) {
        // Invia una richiesta PUT al server per aggiornare il calendario
		String name = "Calendar001"; // per evitare di dover cliccare sulla riga della tabella
        String msg  = AppConfig.getURL() + "api/calendar/" + name + "/" + startDate.toString() + "/" + endDate.toString();
        ResponseEntity<List<String>> response = restTemplate.exchange(msg, HttpMethod.PUT, null, new ParameterizedTypeReference<List<String>>() {});
        if (response.getStatusCode() == HttpStatus.OK) {
            List<String> toReschedule = response.getBody();
            // Mostra un alert con la lista toReschedule
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sale da rischedulare");
                alert.setHeaderText(null);
                alert.setContentText("Le seguenti sale devono essere rischedulate: \n" + String.join(", ", toReschedule));
                alert.showAndWait();
            });
            Platform.runLater(this::mettiDati);
        }
    }
}
    
    