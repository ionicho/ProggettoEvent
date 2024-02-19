package client.windows;

import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import server.AppConfig;
import server.model.*;

/**
 Classe per la gestione della finestra con l'elenco delle camere e la loro disponibilità.
 Con la classe WRoom2Event consente di inviare una request al server e quando 
 riceve la notifica del tutto ok, aggiorna la videata.
 */
public class WRoom2 {

    private TableView<Room> table;
    private RestTemplate restTemplate;
    private WRoom2Event wRoom2Event;
        
    public void start(Stage primaryStage, RestTemplate restTemplate) {
        primaryStage.setTitle("Visualizzazione Camere");
        this.restTemplate = restTemplate;
        this.table = new TableView<>(); // Inizializza table
        this.wRoom2Event = new WRoom2Event(this);
        addColonneStatiche();
        mettiDati();
        Scene scene = new Scene(table, 1200, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void addColonneStatiche() {
        String centrato = "CENTER";
        String aDestra = "CENTER-RIGHT";
        TableColumn<Room, String> cameraCol = StaticCol.creaCol("Camera", "nome", centrato);
        table.getColumns().add(cameraCol);
        TableColumn<Room, RoomType> tipoCol = StaticCol.creaCol("Tipo", "tipo", centrato);
        table.getColumns().add(tipoCol);
        TableColumn<Room, Double> costoCol = StaticCol.creaCol("Costo", "costo", aDestra);
        table.getColumns().add(costoCol);
        TableColumn<Room, Integer> lettiCol = StaticCol.creaCol("N. Letti", "numeroLetti", centrato);
        table.getColumns().add(lettiCol);
    }
    
    private void addColonneDinamiche(Set<String> uniqueDates) {
        // Converti il Set in una List
        List<String> dateList = new ArrayList<>(uniqueDates);      
        // Ordina la lista
        Collections.sort(dateList);       
        // Ora aggiungi le colonne in base all'ordine della lista
        for (String date : dateList) {	
            TableColumn<Room, String> column = new TableColumn<>(date);
            column.setCellValueFactory(new DinamicCol(date));        	 
            column.setCellFactory(col -> wRoom2Event.creaCellaColorate()); 
            table.getColumns().add(column);
        }
    }

    private void mettiDati() {
        // Invia una richiesta GET al server per ottenere i dati di tutte le camere
        String url = AppConfig.getURL() + "api/room";
        Room[] rooms = restTemplate.getForObject(url, Room[].class);
        // Crea un insieme di date uniche
        Set<String> uniqueDates = new HashSet<>();
        if (rooms != null){
            for (Room room : rooms) {
                for (StateDate stateDate : room.getDisponibilita()) {
                    uniqueDates.add(stateDate.getData().toString());
                }
            }
        }
        // Aggiungi colonne dinamiche per le date e gli stati
        addColonneDinamiche(uniqueDates);
        // Popola la tabella con i dati ricevuti
        ObservableList<Room> data = FXCollections.observableArrayList(rooms);
        table.setItems(data);
    }
    
    public void aggiornaTabella() {
        // Rimuovi tutte le righe e colonne esistenti
        table.getItems().clear();
        table.getColumns().clear();
        addColonneStatiche();
        mettiDati();
    }        
}

