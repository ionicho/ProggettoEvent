package client.windows;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import server.AppConfig;
import server.model.*;

public class WCalendarEvent implements EventHandler<MouseEvent> {

	private final RestTemplate restTemplate;
    private final WCalendar wCalendar; //Riferimento a WCalendar

    public WCalendarEvent(WCalendar wCalendar, RestTemplate restTemplate) {
    	this.restTemplate = restTemplate;
        this.wCalendar = wCalendar;
    }
    
    public void mettiDati() {
        Calendar calendar = restTemplate.getForObject(AppConfig.getURL() + "api/calendar", Calendar.class);
        if (calendar != null) {
            ObservableList<StateDate> data = FXCollections.observableArrayList(calendar.getDisponibilita());
            wCalendar.getTable().setItems(data);
        }
    }
   
    @SuppressWarnings("null")
    public void setCalendario(LocalDate startDate, LocalDate endDate) {
        // Invia una richiesta PUT al server per aggiornare il calendario
        String msg  = AppConfig.getURL() + "api/calendar/" + startDate.toString() + "/" + endDate.toString();
        System.out.printf("%s \n", msg);
        ResponseEntity<String> response = restTemplate.exchange(msg, HttpMethod.PUT, null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Platform.runLater(this::mettiDati);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) { //click dx del mouse
           TableCell <StateDate, State> cell = (TableCell) event.getSource();
            StateDate stateDate = cell.getTableRow().getItem();
            LocalDate data = stateDate.getData();
            ContextMenu menuStati = new ContextMenu();
            // Aggiungi solo gli stati CHIUSO e DISPONIBILE al menu
            State[] statiPossibili = {State.CHIUSO, State.DISPONIBILE};
            for (State stato : statiPossibili) {
                MenuItem item = new MenuItem(stato.name());
                item.setOnAction(setStatoData(data, stato));
                menuStati.getItems().add(item);
            }
            menuStati.show((javafx.scene.Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }
    
    @SuppressWarnings("null")
    private EventHandler<ActionEvent> setStatoData(LocalDate data, State stato) {
           return e -> {
               // Crea un nuovo StateDate con la data e lo stato selezionati
               StateDate statoData = new StateDate(data,stato);
               System.out.println("per la data " + data + " selezionato " + statoData);
               // Invia una richiesta PUT del StateDate al tuo server
               HttpEntity<StateDate> request = new HttpEntity<>(statoData);
               ResponseEntity<Void> response = restTemplate.exchange(AppConfig.getURL() + "api/calendar/state", HttpMethod.PUT, request, Void.class);          
               if (response.getStatusCode() == HttpStatus.OK) {
                   // Aggiorna la GUI qui
                   Platform.runLater(this::mettiDati);
               }
           };
       }
   
    public TableCell<StateDate, State> coloraCelle() {
        return new TableCell<StateDate, State>() {
            @Override
            protected void updateItem(State item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.name());
                if (item == null || empty) {setStyle(""); return;}
                // Cambia lo sfondo della cella in base al suo stato
                switch (item) {
                    case DISPONIBILE:
                        setStyle("-fx-alignment: CENTER");
                        break;
                    case CHIUSO:
                        setStyle("-fx-alignment: CENTER; -fx-background-color: #ff7f7f"); // Rosso chiaro
                        break;
                    default:
                        setStyle("-fx-alignment: CENTER");
                        break;
                }
                // Aggiungi un gestore di eventi
                setOnMouseClicked(WCalendarEvent.this);
            }
        };
    }

}