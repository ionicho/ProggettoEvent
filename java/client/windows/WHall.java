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
Classe per la gestione della finestra con l'elenco delle sale e la loro disponibilità.
Con la classe Handler consente di inviare una request al server e quando 
riceve la notifica del tutto ok, aggiorna la videata.
*/
public class WHall {

    private TableView<Hall> table;
    private RestTemplate restTemplate;
    private Handler <Hall> handler;
        
    public void start(Stage primaryStage, RestTemplate restTemplate) {
        primaryStage.setTitle("Visualizzazione Sale");
        this.restTemplate = restTemplate;
        this.table = new TableView<>(); // Inizializza table
        this.handler = new Handler<>(this, restTemplate);
        addColonneStatiche();
        mettiDati();
        Scene scene = new Scene(table, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metodo per ottenere la tabella come un Node
    public TableView<Hall> getTable() {
        return table;
    }

    private void addColonneStatiche() {
        String centrato = "CENTER";
        String aDestra = "CENTER-RIGHT";
        TableColumn<Hall, String> cameraCol = StaticCol.creaCol("Sala", "nome", centrato);
        table.getColumns().add(cameraCol);
        TableColumn<Hall, Double> costoCol = StaticCol.creaCol("Costo", "costo", aDestra);
        table.getColumns().add(costoCol);
        TableColumn<Hall, Integer> postiCol = StaticCol.creaCol("N. Posti", "numeroPosti", centrato);
        table.getColumns().add(postiCol);
    }
    
    private void addColonneDinamiche(Set<String> uniqueDates) {
        // Converti il Set in una List
        List<String> dateList = new ArrayList<>(uniqueDates);      
        // Ordina la lista
        Collections.sort(dateList);       
        // Ora aggiungi le colonne in base all'ordine della lista
        for (String date : dateList) {	
            TableColumn<Hall, String> column = new TableColumn<>(date);
            column.setCellValueFactory(new DinamicCol<>(date));        	 
            column.setCellFactory(col -> handler.creaCellaColorate()); 
            table.getColumns().add(column);
        }
    }

    private void mettiDati() {
        // Invia una richiesta GET al server per ottenere i dati di tutte le sale
        String url = AppConfig.getURL() + "api/hall";
        Hall[] halls = restTemplate.getForObject(url, Hall[].class);
        // Crea un insieme di date uniche
        Set<String> uniqueDates = new HashSet<>();
        if (halls != null){
            for (Hall curr : halls) {
                for (StateDate stateDate : curr.getDisponibilita()) {
                    uniqueDates.add(stateDate.getData().toString());
                }
            }
        }
        // Aggiungi colonne dinamiche per le date e gli stati
        addColonneDinamiche(uniqueDates);
        // Popola la tabella con i dati ricevuti
        ObservableList<Hall> data = FXCollections.observableArrayList(halls);
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

