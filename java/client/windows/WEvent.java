package client.windows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import server.controller.ServerException;
import server.model.*;
import server.service.LocalDateTypeAdapter;
import server.service.LocalTimeTypeAdapter;
import server.service.StateDateTypeAdapter;

public class WEvent extends SuperWin {
	
	private RestTemplate restTemplate;
	private Event evento;
	private GridPane grid;  // Aggiungi un campo per la griglia
	
	private TextField cercaF = creaTextField();
	private TextField idField = creaTextField();
	private TextField msgF = creaTextField();
    private TextField nomeOrgF = creaTextField();
    private TextField costoParF = creaTextField();
    private TextField oraIniF  = creaHourField();
    private TextField oraFinF  = creaHourField();
    private DatePicker dataF = new DatePicker();
    private TextField interventiF  = creaTextField(); //ListView<>();
	private Button newButton = creaButton("Crea Nuovo Evento");
    private Button saveButton = creaButton("Salva / Modifica Evento");
    private final Label cercaL = creaLabel("ID da cercare:");
    private final Label msgL = creaLabel("Messaggio:");
    private final Label idLabel = creaLabel("ID evento:");
    private final Label nomeOrgL = creaLabel("Nome Organizzatore:");
    private final Label costoParL = creaLabel("Costo Partecipazione:");
    private final Label dataL = creaLabel("Data:");
    private final Label oraIniL = creaLabel("Ora Inizio:");
    private final Label oraFinL = creaLabel("Ora Fine:");
    private final Label interventiL = creaLabel("Elenco Interventi:");
    
    private void setLayout() {
        // Aggiungi le etichette e i campi nel layout a griglia
        grid.add(cercaL,0,0);
        grid.add(cercaF, 1, 0);
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
        grid.add(interventiF, 1, 9);
    }

	public void start(Stage eventStage, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	    eventStage.setTitle("Gestione Eventi");
	    grid = creaGriglia();
	    Scene scene = new Scene(grid);
	    eventStage.setScene(scene);
	    eventStage.show();
	}

	private Event getEvento(String id, RestTemplate restTemplate) {
	    String url = "http://localhost:8080/api/eventi/" + id;

	    // Crea un'istanza di HttpHeaders e imposta il tipo di contenuto su 'application/json'
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    // Crea un oggetto HttpEntity con gli headers
	    HttpEntity<String> entity = new HttpEntity<>(headers);

	    // Invia la richiesta al server e ottieni la risposta
	    ResponseEntity<Event> response = restTemplate.exchange(url, HttpMethod.GET, entity, Event.class);

	    // Restituisci l'evento dalla risposta
	    return response.getBody();
	}

	/*
	
	private Event getEvento(String id, RestTemplate restTemplate) {
		String url = "http://localhost:8080/api/eventi/" + id;
		return restTemplate.getForObject(url, Event.class);
	}
	*/
	
	private Event addEvento(RestTemplate restTemplate) {
		String url = "http://localhost:8080/api/eventi/nuovo";
		return  restTemplate.getForObject(url, Event.class);	
	}
	
	private Event updateEvento(Event evento, RestTemplate restTemplate) {
	    String url = "http://localhost:8080/api/eventi/" + evento.getId();

	    // Crea un'istanza di Gson con gli adattatori registrati
	    Gson gson = new GsonBuilder()
	        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
	        .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
	        .registerTypeAdapter(StateDate.class, new StateDateTypeAdapter())
	        .create();

	    // Usa questa istanza di Gson per la serializzazione
	    String json = gson.toJson(evento);
	    System.out.printf("WEVENT json= %s\n", json);

	    // Crea un oggetto HttpHeaders e imposta il tipo di contenuto su 'application/json'
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    // Crea un oggetto HttpEntity con il tuo JSON e gli headers
	    HttpEntity<String> entity = new HttpEntity<>(json, headers);

	    // Invia la richiesta al server e ottieni la risposta
	    ResponseEntity<Event> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Event.class);

	    // Restituisci l'evento dalla risposta
	    return response.getBody();
	}



	private GridPane creaGriglia() {
		    grid = new GridPane();
	        grid.setHgap(.10 * WIDTH);
	        grid.setVgap(.10 * WIDTH);
	        grid.setPrefWidth(2.3* WIDTH);
	        grid.setPadding(new Insets(.10 * WIDTH, .10 * WIDTH, .10 * WIDTH, .10 * WIDTH));  // Imposta un margine su tutti i lati
			idField.setEditable(false); 
			msgF.setEditable(false); // Impedisce all'utente di modificare il campo di risposta
	        dataF.setPrefWidth(WIDTH);        

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
	                System.out.printf("\n DOPO LA GET %s\n", this.evento.toString());
	            } catch (ServerException | HttpClientErrorException.NotFound ex) {
	                msgF.setText("Nessun evento trovato con l'ID specificato.");
	                idField.setText(cercaF.getText());
	                nomeOrgF.setText("");
	                costoParF.setText("");		    
	                oraIniF.setText("");
	                oraFinF.setText("");
	                dataF.setValue(null);
	            }		    
	        });
	        
	        // gestisce la logica di creazione di un evento
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
				} catch (ServerException | HttpClientErrorException.NotFound ex) {
		            msgF.setText("La creazione del nuovo evento non è riuscita.");
				}
			});

			// gestisce la logica di salvataggio delle modifiche di un evento
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
	            try {
	            	System.out.printf("SAVE BUTTON evento =%s\n", this.evento.toString());
	            	this.evento = updateEvento(this.evento,restTemplate);	
				} catch (ServerException | HttpClientErrorException.NotFound ex) {
		            msgF.setText("Il salvataggio dell'evento non è riuscita.");
				}
	        });
	        
	        setLayout();
	        return grid;
	    }
}
