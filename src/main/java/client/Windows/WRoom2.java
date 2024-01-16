package client.Windows;

import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import java.util.*;

import server.model.*;

public class WRoom2 {

	    private TableView<ResourceRoom> table;
	    private RestTemplate restTemplate;
	    //private WRoom2eHandler handler;
	
	    public void start(Stage primaryStage, RestTemplate restTemplate) {
	        primaryStage.setTitle("Visualizzazione Camere");
	        this.restTemplate = restTemplate;
	        this.table = new TableView<>(); // Inizializza table
	        addColonneStatiche();
	        mettiDati();
	
	        Scene scene = new Scene(table, 600, 400);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }
        

        private void addColonneStatiche() {
	        	TableColumn<ResourceRoom, String> cameraCol = StaticColumn.createColumn("Camera", "nome", "CENTER");
	        	table.getColumns().add(cameraCol);
	        	TableColumn<ResourceRoom, RoomType> tipoCol = StaticColumn.createColumn("Tipo", "tipo", "CENTER");
	        	table.getColumns().add(tipoCol);
	        	TableColumn<ResourceRoom, Double> costoCol = StaticColumn.createColumn("Costo", "costo", "CENTER-RIGHT");
	        	table.getColumns().add(costoCol);
	        	TableColumn<ResourceRoom, Integer> lettiCol = StaticColumn.createColumn("N. Letti", "numeroLetti", "CENTER");
	        	table.getColumns().add(lettiCol);
        }

        private void addColonneDinamiche(Set<String> uniqueDates) {
            for (String date : uniqueDates) {
                TableColumn<ResourceRoom, String> column = new TableColumn<>(date);
                column.setCellValueFactory(new DinamicColumn(date));
                column.setCellFactory(col -> coloraCelle(date));
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
        private TableCell<ResourceRoom, String> coloraCelle(String date) {
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
                        	setStyle("-fx-alignment: CENTER; -fx-background-color: #ff7f7f"); // Rosso chiaro
                            break;
                        case "INUSO":
                        	setStyle("-fx-alignment: CENTER; -fx-background-color: #ffff99"); // Giallo chiaro
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


