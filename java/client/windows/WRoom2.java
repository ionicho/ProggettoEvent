package client.windows;

import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import server.model.*;

/**
 Classe per la gestione della finestra con l'elenco delle camere e la loro disponibilità.
 Con la classe WRoom2eHandler consente di inviare una request al server e quando 
 riceve la notifica del tutto ok, aggiorna la videata.
 */
public class WRoom2 {

	    private TableView<ResourceRoom> table;
	    private RestTemplate restTemplate;
	    
	    public void start(Stage primaryStage, RestTemplate restTemplate) {
	        primaryStage.setTitle("Visualizzazione Camere");
	        this.restTemplate = restTemplate;
	        this.table = new TableView<>(); // Inizializza table
	        addColonneStatiche();
	        mettiDati();
	
	        Scene scene = new Scene(table, 1200, 400);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }
        

	    private void addColonneStatiche() {

	    	String centrato = "CENTER";
	    	String aDestra = "CENTER-RIGHT";
	        TableColumn<ResourceRoom, String> cameraCol = StaticColumn.createColumn("Camera", "nome", centrato);
	        table.getColumns().add(cameraCol);

	        TableColumn<ResourceRoom, RoomType> tipoCol = StaticColumn.createColumn("Tipo", "tipo", centrato);
	        table.getColumns().add(tipoCol);

	        TableColumn<ResourceRoom, Double> costoCol = StaticColumn.createColumn("Costo", "costo", aDestra);
	        table.getColumns().add(costoCol);

	        TableColumn<ResourceRoom, Integer> lettiCol = StaticColumn.createColumn("N. Letti", "numeroLetti", centrato);
	        table.getColumns().add(lettiCol);
	    }

	   
	    private void addColonneDinamiche(Set<String> uniqueDates) {
	        // Converti il Set in una List
	        List<String> dateList = new ArrayList<>(uniqueDates);
	        
	        // Ordina la lista
	        Collections.sort(dateList);
	        
	        // Ora aggiungi le colonne in base all'ordine della lista
	        for (String date : dateList) {
	            TableColumn<ResourceRoom, String> column = new TableColumn<>(date);
	            column.setCellValueFactory(new DinamicColumn(date));
	            column.setCellFactory(col -> coloraCelle());
	            table.getColumns().add(column);
	        }
	    }


        private void mettiDati() {
            // Invia una richiesta GET al server per ottenere i dati di tutte le camere
            ResourceRoom[] rooms = restTemplate.getForObject("http://localhost:8080/api/room", ResourceRoom[].class);

            // Crea un insieme di date uniche
            Set<String> uniqueDates = new HashSet<>();
            for (ResourceRoom room : rooms) {
                for (StateDate stateDate : room.getDisponibilita()) {
                    uniqueDates.add(stateDate.getData().toString());
                }
            }

            // Aggiungi colonne dinamiche per le date e gli stati
            addColonneDinamiche(uniqueDates);

            // Popola la tabella con i dati ricevuti
            ObservableList<ResourceRoom> data = FXCollections.observableArrayList(rooms);
            table.setItems(data);
        }
        
        public void aggiornaTabella() {
            // Rimuovi tutte le righe e colonne esistenti
            table.getItems().clear();
            table.getColumns().clear();
            addColonneStatiche();
            mettiDati();
        }
        
            
        ////////////////// inizio classe anonima /////////////////////////   
        private TableCell<ResourceRoom, String> coloraCelle() {
        		WRoom2 thisWRoom2 = this; //crea un riferimento alla classe esterna 
            return new TableCell<ResourceRoom, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                    if (item == null || empty) {setStyle(""); return;}
                    // Cambia lo sfondo della cella in base al suo stato
                    switch (item) {
                        case "DISPONIBILE":
                        	setStyle("-fx-alignment: CENTER; -fx-background-color: #90ee90"); // Verde chiaro
                            break;
                        case "PRENOTATA":
                        	setStyle("-fx-alignment: CENTER; -fx-background-color: #ffff99"); // Giallo chiaro
                            break;
                        case "INUSO":
                        	setStyle("-fx-alignment: CENTER; -fx-background-color: #ff7f7f"); // Rosso chiaro
                            break;
                        case "PULIZIA":
                        	setStyle("-fx-alignment: CENTER; -fx-background-color: #d3d3d3"); // Grigio chiaro
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                    // Aggiungo un gestore di eventi
                    setOnMouseClicked(new WRoom2eHandler(thisWRoom2));
                }
            };
        }
    ////////////////// fine classe anonima /////////////////////////   
    
  }


