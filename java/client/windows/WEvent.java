package client.windows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.client.*;

import javafx.application.Platform;
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
	private List<Event> eventi;
    private GridPane grid;
    private Label cercaL, msgL, idLabel, nomeOrgL, costoParL, numParPreL, cateringL, dataL, oraIniL, oraFinL, interventiL;
    private TextField cercaF, idField, msgF, nomeOrgF, costoParF, numParPreF, oraIniF, oraFinF;
    private ComboBox<String> cateringF;
	private DatePicker dataF;
    private TableView<Speech> interventiLista;
    private TableColumn<Speech, String> titolo, relatore, descrizione, elimina;
    private Button firstButton, prevButton, nextButton, lastButton, newButton, saveButton, newSpeech;
    private static final String ORAMINUTI = "HH:mm";
	
	public void start(Stage eventStage, RestTemplate restTemplate) {
		setFields();
		this.url = AppConfig.getURL() +"api/eventi";
		this.restTemplate = restTemplate;
	    eventStage.setTitle("Gestione Eventi");
	    grid = creaGriglia();
	    Scene scene = new Scene(grid);
	    eventStage.setScene(scene);
	    eventStage.show();
		getEventi(restTemplate);
		evento = eventi.get(0);
		populateFields(evento);
	}

	@SuppressWarnings("null")
	private void getEventi(RestTemplate restTemplate) {
		this.eventi = Arrays.asList(restTemplate.getForObject(url, Event[].class));
		System.out.printf("EVENTI = %s\n", eventi);
	}
	
	/** Restituisce l'evento con l'ID specificato */
	private Event getEvento(String id, RestTemplate restTemplate) {
		return restTemplate.getForObject(url+"/"+id, Event.class);
	}

	/** Aggiunge un nuovo evento */
	private Event addEvento(RestTemplate restTemplate) {
		return  restTemplate.getForObject(url+"/nuovo", Event.class);	
	}
	
	/** Aggiorna un evento e restituisce l'evento aggiornato */
	private Event updateEvento(Event evento, RestTemplate restTemplate) {
		restTemplate.put(url+"/"+evento.getId(), evento);
		return getEvento(evento.getId(), restTemplate);
	}

	/** Inizializza i campi */
	private void setFields() {
        cercaF = creaTextField();
        idField = creaTextField();
        msgF = creaTextField();
        nomeOrgF = creaTextField();
        costoParF = creaTextField();
		numParPreF = creaTextField();
		String[] cateringOptions = Arrays.stream(TipoCatering.values())
		.map(TipoCatering::name)
		.toArray(String[]::new);
		cateringF = creaComboBox(cateringOptions);
        oraIniF  = creaHourField();
        oraFinF  = creaHourField();
        dataF = new DatePicker();
        interventiLista = new TableView<>();
		interventiLista.setEditable(true);
        titolo = new TableColumn<>("Titolo");
		titolo.setCellFactory(TextFieldTableCell.forTableColumn()); //rende modificabile
        relatore = new TableColumn<>("Relatore");
		relatore.setCellFactory(TextFieldTableCell.forTableColumn());
        descrizione = new TableColumn<>("Descrizione");
		descrizione.setCellFactory(TextFieldTableCell.forTableColumn());
		elimina = new TableColumn<>("Azione");
		setElimina();
		firstButton = creaButton("Primo Evento");
		prevButton = creaButton("Evento Precedente");
		nextButton = creaButton("Prossimo Evento");
		lastButton = creaButton("Ultimo Evento");
		newButton = creaButton("Crea Nuovo Evento");
        saveButton = creaButton("Salva / Modifica Evento");
        newSpeech = creaButton("Crea nuovo Intervento");
        cercaL = creaLabel("ID da cercare:");
        msgL = creaLabel("Messaggio:");
        idLabel = creaLabel("ID evento:");
        nomeOrgL = creaLabel("Nome Organizzatore:");
        costoParL = creaLabel("Costo Partecipazione:");
		numParPreL = creaLabel("Partecipanti Previsti:");
		cateringL = creaLabel("Catering:");
        dataL = creaLabel("Data:");
        oraIniL = creaLabel("Ora Inizio:");
        oraFinL = creaLabel("Ora Fine:");
        interventiL = creaLabel("Elenco Interventi:");
    }

	/** Pulisce i campi */
	private void clearFields() {
		idField.setText("");
		nomeOrgF.setText("");
		costoParF.setText("0.0");
		numParPreF.setText("0");
		cateringF.getItems().clear();
		oraIniF.setText("");
		oraFinF.setText("");
		dataF.setValue(null);
		interventiLista.getItems().clear();
	}
	
	private void populateFields(Event evento) {
		idField.setText(evento.getId() != null ? evento.getId() : "");
		nomeOrgF.setText(evento.getNomeOrganizzatore() != null ? evento.getNomeOrganizzatore() : "");
		costoParF.setText(evento.getCostoPartecipazione() != null ? String.valueOf(evento.getCostoPartecipazione()) : "");
		numParPreF.setText(evento.getPartPrevisti() != null ? String.valueOf(evento.getPartPrevisti()) : "");
		cateringF.setValue(evento.getCatering() != null ? evento.getCatering().toString() : null);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ORAMINUTI);
		oraIniF.setText(evento.getOraInizio() != null ? evento.getOraInizio().format(formatter) : "");
		oraFinF.setText(evento.getOraFine() != null ? evento.getOraFine().format(formatter) : "");
		dataF.setValue(evento.getData() != null ? evento.getData() : LocalDate.now());
		titolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
		relatore.setCellValueFactory(new PropertyValueFactory<>("relatore"));
		descrizione.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
		interventiLista.getItems().clear();
		List<Speech> interventi = evento.getElencoInterventi();
		if (interventi != null) {
			for (Speech speech : interventi) {
				interventiLista.getItems().add(speech);
			}
		}
	}

	private void populateEvent() {
		if (this.evento != null) {
			this.evento.setNomeOrganizzatore(nomeOrgF.getText());
			this.evento.setCostoPartecipazione(Double.parseDouble(costoParF.getText()));
			this.evento.setPartPrevisti(Integer.parseInt(numParPreF.getText()));
			this.evento.setCatering(cateringF.getValue() == null || cateringF.getValue().equals("") ? 
				TipoCatering.COFFEE_BREAK : TipoCatering.valueOf(cateringF.getValue()));
			this.evento.setOraInizio(LocalTime.parse(oraIniF.getText(), DateTimeFormatter.ofPattern(ORAMINUTI)));
			this.evento.setOraFine(LocalTime.parse(oraFinF.getText(), DateTimeFormatter.ofPattern(ORAMINUTI)));
			this.evento.setData(dataF.getValue());
			this.evento.getElencoInterventi().clear();
			this.evento.addInterventi(interventiLista.getItems());
		}
	}

	/** Imposta il layout */
    private void setLayout() {
		int row=0;
		grid = new GridPane();
		grid.setHgap(.10 * WIDTH);
		grid.setVgap(.10 * WIDTH);
		grid.setPrefWidth(6.2* WIDTH);
		grid.setPadding(new Insets(.10 * WIDTH, .10 * WIDTH, .10 * WIDTH, .10 * WIDTH));  // Imposta un margine su tutti i lati
		idField.setEditable(false); 
		msgF.setEditable(false); // Impedisce all'utente di modificare il campo di risposta
		dataF.setPrefWidth(WIDTH);
		titolo.setPrefWidth(WIDTH); 
		relatore.setPrefWidth(WIDTH);
		descrizione.setPrefWidth(3.55*WIDTH);
		elimina.setPrefWidth(.42*WIDTH);
		elimina.setSortable(false);
        grid.add(cercaL,0,row);
        grid.add(cercaF, 1, row);
        grid.add(msgL, 0, ++row);
        grid.add(msgF, 1,row);
        grid.add(idLabel, 0, ++row);
        grid.add(idField, 1, row);
        grid.add(nomeOrgL, 0, ++row);
        grid.add(nomeOrgF, 1, row);
        grid.add(costoParL, 0, ++row);
        grid.add(costoParF, 1, row);
		grid.add(numParPreL, 0, ++row);
		grid.add(numParPreF, 1, row);
		grid.add(cateringL, 0, ++row);
		grid.add(cateringF, 1, row);
        grid.add(dataL, 0, ++row);
        grid.add(dataF, 1, row);
        grid.add(oraIniL, 0, ++row);
        grid.add(oraIniF, 1, row);
        grid.add(oraFinL, 0, ++row);
        grid.add(oraFinF, 1, row);
        grid.add(interventiL, 0, ++row);
		grid.add(newSpeech,1,row);
		interventiLista.getColumns().add(titolo);
		interventiLista.getColumns().add(relatore);
		interventiLista.getColumns().add(descrizione);
		interventiLista.getColumns().add(elimina);
		grid.add(interventiLista,0,++row,6,1);
		GridPane.setHalignment(interventiLista, HPos.CENTER);
		interventiLista.setPrefHeight(200);
		grid.add(firstButton, 0, ++row);
		grid.add(prevButton, 1, row);
		grid.add(nextButton, 2, row);
		grid.add(lastButton, 3, row);	
		grid.add(newButton, 4, row);
        grid.add(saveButton, 5, row);
    }

	/** Crea la griglia */
	private GridPane creaGriglia() {
		Map<Integer, Speech> tempSpeeches = new HashMap<>();
				
		// Gestisce la logica degli spostamenti dell'evento
		firstButtonAction();
		prevButtonAction();
		nextButtonAction();
		lastButtonAction();

		// Gestisce la logica di creazione di un evento
		newButton.setOnAction(e -> {
			try {
				System.out.printf("WINDOW creaButton\n");
				this.evento =  addEvento(restTemplate);
				cercaF.setText("");
				clearFields();
				msgF.setText("Creato nuovo evento.");
			} catch (ServerException | HttpClientErrorException.NotFound ex) {
				msgF.setText("La creazione del nuovo evento non è riuscita.");
			}
		});
		
		// Gestisce la logica della ricerca di un evento
		cercaF.setPromptText("Inserisci l'ID dell'evento");
		cercaF.setOnAction(e -> {
			try {
				this.evento = getEvento(cercaF.getText(), restTemplate);
				populateFields(evento);
			} catch (ServerException | HttpClientErrorException.NotFound ex) {
				msgF.setText("Nessun evento trovato con l'ID specificato.");
				clearFields();
				idField.setText(cercaF.getText());
			}		    
		});

		// Gestisce la logica di salvataggio delle modifiche di un evento
		saveButton.setOnAction(e -> {
			try {
				System.out.printf("SAVE BUTTON evento =%s\n", this.evento.toString());
				for (Map.Entry<Integer, Speech> entry : tempSpeeches.entrySet()) {
					int index = entry.getKey();
					Speech speech = entry.getValue();
					if (index < this.evento.getElencoInterventi().size()) {
						this.evento.getElencoInterventi().set(index, speech);
					} else {
						this.evento.addIntervento(speech);
					}
				}
				populateEvent();
				this.evento = updateEvento(this.evento,restTemplate);
				populateFields(this.evento);
			} catch (ServerException | HttpClientErrorException.NotFound ex) {
				msgF.setText("Il salvataggio dell'evento non è riuscita.");
				clearFields();
			}
		});

		newSpeech.setOnAction(e -> {
			Speech speech = new Speech("","","");
			interventiLista.getItems().add(speech);
			int index = interventiLista.getItems().indexOf(speech);
			tempSpeeches.put(index, speech);
			Platform.runLater(() -> interventiLista.edit(index, titolo));
		});

		titolo.setOnEditCommit(e -> {
			Speech speech = e.getRowValue();
			speech.setTitolo(e.getNewValue());
			int index = interventiLista.getItems().indexOf(speech);
			tempSpeeches.put(index, speech);
			Platform.runLater(() -> interventiLista.edit(index, relatore));
		});

		relatore.setOnEditCommit(e -> {
			Speech speech = e.getRowValue();
			speech.setRelatore(e.getNewValue());
			int index = interventiLista.getItems().indexOf(speech);
			tempSpeeches.put(index, speech);
			Platform.runLater(() -> interventiLista.edit(index, descrizione));
		});

		descrizione.setOnEditCommit(e -> {
			Speech speech = e.getRowValue();
			speech.setDescrizione(e.getNewValue());
			int index = interventiLista.getItems().indexOf(speech);
			tempSpeeches.put(index, speech);
			Platform.runLater(() -> interventiLista.edit(index, titolo));
		});
		
		setLayout();
		return grid;
	}

	private void firstButtonAction() {
		firstButton.setOnAction(e -> {
			this.evento = eventi.get(0);
			populateFields(evento);
		});
	}

	private void prevButtonAction(){
		prevButton.setOnAction(e -> {
			int index = eventi.indexOf(this.evento);
			if (index > 0) {
				this.evento = eventi.get(index - 1);
				populateFields(evento);
			}
		});
	}

	private void nextButtonAction(){
		nextButton.setOnAction(e -> {
			int index = eventi.indexOf(this.evento);
			if (index < eventi.size() - 1) {
				this.evento = eventi.get(index + 1);
				populateFields(evento);
			}
		});
	}

	private void lastButtonAction() {
		lastButton.setOnAction(e -> {
			this.evento = eventi.get(eventi.size() - 1);
			populateFields(evento);
		});
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

}