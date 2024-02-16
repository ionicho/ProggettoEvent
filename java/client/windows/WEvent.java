package client.windows;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.client.*;
import org.springframework.http.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import server.AppConfig;
import server.controller.ServerException;
import server.model.*;
import java.util.*;

public class WEvent extends SuperWin {
	
	private String url;
    private RestTemplate restTemplate;
    private Event evento;
    private GridPane grid;
    private Label fakeL, cercaL, msgL, idLabel, nomeOrgL, costoParL, dataL, oraIniL, oraFinL, interventiL;
    private TextField cercaF, idField, msgF, nomeOrgF, costoParF, oraIniF, oraFinF;
    private DatePicker dataF;
    private TableView<Speech> interventiLista;
    private TableColumn<Speech, String> titolo, relatore, descrizione, elimina;
    private Button newButton, saveButton, newSpeech;
	
	public void start(Stage eventStage, RestTemplate restTemplate) {
		setFields();
		this.url = AppConfig.getURL() +"api/eventi/";
		this.restTemplate = restTemplate;
	    eventStage.setTitle("Gestione Eventi");
	    grid = creaGriglia();
	    Scene scene = new Scene(grid);
	    eventStage.setScene(scene);
	    eventStage.show();
	}
  
	/** Inizializza i campi */
	private void setFields() {
        cercaF = creaTextField();
        idField = creaTextField();
        msgF = creaTextField();
        nomeOrgF = creaTextField();
        costoParF = creaTextField();
        oraIniF  = creaHourField();
        oraFinF  = creaHourField();
        dataF = new DatePicker();
        interventiLista = new TableView<>();
        titolo = new TableColumn<>("Titolo");
		titolo.setCellFactory(TextFieldTableCell.forTableColumn()); //rende modificabile
        relatore = new TableColumn<>("Relatore");
		relatore.setCellFactory(TextFieldTableCell.forTableColumn());
        descrizione = new TableColumn<>("Descrizione");
		descrizione.setCellFactory(TextFieldTableCell.forTableColumn());
		elimina = new TableColumn<>("Azione");
		setElimina();
		newButton = creaButton("Crea Nuovo Evento");
        saveButton = creaButton("Salva / Modifica Evento");
        newSpeech = creaButton("Crea nuovo Intervento");
        fakeL = creaLabel("");
        cercaL = creaLabel("ID da cercare:");
        msgL = creaLabel("Messaggio:");
        idLabel = creaLabel("ID evento:");
        nomeOrgL = creaLabel("Nome Organizzatore:");
        costoParL = creaLabel("Costo Partecipazione:");
        dataL = creaLabel("Data:");
        oraIniL = creaLabel("Ora Inizio:");
        oraFinL = creaLabel("Ora Fine:");
        interventiL = creaLabel("Elenco Interventi:");
    }

	/** Imposta il layout */
    private void setLayout() {
        // Aggiungi le etichette e i campi nel layout a griglia
        grid.add(cercaL,0,0);
        grid.add(cercaF, 1, 0);
		grid.add(fakeL,1,2);
        grid.add(msgL, 0, 1);
        grid.add(msgF, 1,1);
        grid.add(newButton, 0, 2);
        grid.add(saveButton, 1, 2);
        grid.add(idLabel, 0, 3);
        grid.add(idField, 1, 3);
        grid.add(nomeOrgL, 0, 4);
        grid.add(nomeOrgF, 1, 4);
        grid.add(costoParL, 0, 5);
        grid.add(costoParF, 1, 5);
        grid.add(dataL, 0, 6);
        grid.add(dataF, 1, 6);
        grid.add(oraIniL, 0, 7);
        grid.add(oraIniF, 1, 7);
        grid.add(oraFinL, 0, 8);
        grid.add(oraFinF, 1, 8);
        grid.add(interventiL, 0, 9);
		grid.add(newSpeech,1,9);
		interventiLista.getColumns().add(titolo);
		interventiLista.getColumns().add(relatore);
		interventiLista.getColumns().add(descrizione);
		interventiLista.getColumns().add(elimina);
		grid.add(interventiLista,0,10,2,1);
		GridPane.setHalignment(interventiLista, HPos.CENTER);
		GridPane.setHgrow(interventiLista, Priority.ALWAYS);
    }

	@SuppressWarnings("null")
	private Event getEvento(String id, RestTemplate restTemplate) {

	    // Crea un'istanza di HttpHeaders e imposta il tipo di contenuto su 'application/json'
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    // Crea un oggetto HttpEntity con gli headers
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			// Invia la richiesta al server e ottieni la risposta
			ResponseEntity<Event> response = restTemplate.exchange(url+id, HttpMethod.GET, entity, Event.class);
			System.out.printf("Risposta ricevuta prima della deserializzazione%s\n",response);
			Event evento = response.getBody();
			System.out.println("Event dopo deserializzazione: " + evento);

			return evento;
		} catch (Exception e) {
			e.printStackTrace(); // Gestisci l'eccezione in modo appropriato
			return null; // O gestisci l'eccezione e restituisci un valore predefinito
		}
	}

	/** Gestisce la logica della rimozione (dalla maschera) degli interventi*/
	private void setElimina() {
		elimina.setCellFactory(param -> new TableCell<Speech, String>() {
			final Button btn = new Button("Elimina");
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
					setText(null);
				} else {
					btn.setOnAction(event -> {
						Speech speech = getTableView().getItems().get(getIndex());
						getTableView().getItems().remove(speech);
					});
					setGraphic(btn);
					setText(null);
				}
			}
		});
	}


	/*
	
	private Event getEvento(String id, RestTemplate restTemplate) {
		String url = "http://localhost:8080/api/eventi/" + id;
		return restTemplate.getForObject(url, Event.class);
	}
	*/
	
	private Event addEvento(RestTemplate restTemplate) {
		return  restTemplate.getForObject(url+"nuovo", Event.class);	
	}
	
	@SuppressWarnings("null")
	private Event updateEvento(Event evento, RestTemplate restTemplate) {
		ResponseEntity<Event> response = restTemplate.exchange(url+evento.getId(), HttpMethod.PUT, new HttpEntity<>(evento), Event.class);
		return response.getBody();
	}

	private GridPane creaGriglia() {
		    grid = new GridPane();
	        grid.setHgap(.10 * WIDTH);
	        grid.setVgap(.10 * WIDTH);
	        grid.setPrefWidth(3.2* WIDTH);
	        grid.setPadding(new Insets(.10 * WIDTH, .10 * WIDTH, .10 * WIDTH, .10 * WIDTH));  // Imposta un margine su tutti i lati
			idField.setEditable(false); 
			msgF.setEditable(false); // Impedisce all'utente di modificare il campo di risposta
	        dataF.setPrefWidth(WIDTH);
			titolo.setPrefWidth(.6*WIDTH); 
			relatore.setPrefWidth(.6*WIDTH);
			descrizione.setPrefWidth(1.35*WIDTH);
			elimina.setPrefWidth(.42*WIDTH);

			// Gestisce la logica della ricerca di un evento
	        cercaF.setPromptText("Inserisci l'ID dell'evento");
	        cercaF.setOnAction(e -> {
	            try {
	                this.evento = getEvento(cercaF.getText(), restTemplate);
	                idField.setText(evento.getId());
	                nomeOrgF.setText(evento.getNomeOrganizzatore());                
	                costoParF.setText(String.valueOf(evento.getCosto()));		    
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	                oraIniF.setText(evento.getOraInizio().format(formatter));
	                oraFinF.setText(evento.getOraFine().format(formatter));
	                dataF.setValue(evento.getData());
					titolo.setCellValueFactory(new PropertyValueFactory<Speech, String>("titolo"));
					relatore.setCellValueFactory(new PropertyValueFactory<Speech, String>("relatore"));
					descrizione.setCellValueFactory(new PropertyValueFactory<Speech, String>("descrizione"));
					interventiLista.getItems().clear();
					List<Speech> interventi = evento.getElencoInterventi();
					for (Speech speech : interventi) {
						interventiLista.getItems().add(speech);
					}
	            } catch (ServerException | HttpClientErrorException.NotFound ex) {
	                msgF.setText("Nessun evento trovato con l'ID specificato.");
	                idField.setText(cercaF.getText());
	                nomeOrgF.setText("");
	                costoParF.setText("");		    
	                oraIniF.setText("");
	                oraFinF.setText("");
	                dataF.setValue(null);
					interventiLista.getItems().clear();
	            }		    
	        });
	        
	        // Gestisce la logica di creazione di un evento
			newButton.setOnAction(e -> {
				try {
					System.out.printf("WINDOW creaButton\n");
					this.evento =  addEvento(restTemplate);
					cercaF.setText("");
					idField.setText(evento.getId());
	                nomeOrgF.setText("");
	                costoParF.setText("");		    
	                oraIniF.setText("");
	                oraFinF.setText("");
	                dataF.setValue(null);
		            msgF.setText("Creato nuovo evento.");
					interventiLista.getItems().clear();
				} catch (ServerException | HttpClientErrorException.NotFound ex) {
		            msgF.setText("La creazione del nuovo evento non è riuscita.");
				}
			});

			// Gestisce la logica di salvataggio delle modifiche di un evento
	        saveButton.setOnAction(e -> {
	        	if (!nomeOrgF.getText().equals("")) 
	        		this.evento.setNomeOrg(nomeOrgF.getText());
	        	if (!costoParF.getText().equals("")) 
	        		this.evento.setCostoPar(Integer.parseInt(costoParF.getText()));
	        	if (dataF.getValue() != null) 
	        		this.evento.setData(dataF.getValue());
	            if (!oraIniF.getText().equals("")) 
	            	this.evento.setOraInizio(LocalTime.parse(oraIniF.getText()));
	            if (!oraFinF.getText().equals("")) 
	            	this.evento.setOraFine(LocalTime.parse(oraFinF.getText()));
				if (!interventiLista.getItems().isEmpty()) {
					evento.addInterventi(new ArrayList<>(interventiLista.getItems()));
				}
	            try {
	            	System.out.printf("SAVE BUTTON evento =%s\n", this.evento.toString());
	            	this.evento = updateEvento(this.evento,restTemplate);	
				} catch (ServerException | HttpClientErrorException.NotFound ex) {
		            msgF.setText("Il salvataggio dell'evento non è riuscita.");
				}
			});

			newSpeech.setOnAction(e -> {
				Speech speech = new Speech();
				// Imposta gli attributi del discorso
				interventiLista.getItems().add(speech);
			});

			titolo.setOnEditCommit(e -> {
				Speech speech = e.getRowValue();
				speech.setTitolo(e.getNewValue());
			});

			relatore.setOnEditCommit(e -> {
				Speech speech = e.getRowValue();
				speech.setRelatore(e.getNewValue());
			});

			descrizione.setOnEditCommit(e -> {
				Speech speech = e.getRowValue();
				speech.setDescrizione(e.getNewValue());
			});
	        
			newSpeech.setOnAction(e -> {
				Speech speech = new Speech();
				interventiLista.getItems().add(speech);
			});

	        setLayout();
	        return grid;
	    }
}
