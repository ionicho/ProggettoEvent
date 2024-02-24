package client.windows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.client.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import server.controller.SystemException;
import server.model.*;
import java.util.*;

public class WEvent extends WEventLayout implements WEventRest {
	
    private RestTemplate restTemplate;
    private Event evento;
	private WHall wHall;//sottofinestra con le sale
	private Node nHall;
	
	public void start(Stage eventStage, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		setFields(eventStage);
	    eventStage.setTitle("Gestione Eventi");
	    grid = creaGriglia();
	    Scene scene = new Scene(grid);
	    eventStage.setScene(scene);
	    eventStage.show();
	    this.eventi = getEventi(restTemplate);
		evento = eventi.get(0);
		populateFields(evento);
	}
	
	/** Inizializza i campi */
	protected void setFields(Stage stage) {
        super.setFields(stage);
		setElimina();
		Stage hallStage = new Stage();
		hallStage.initOwner(stage); // Imposta la finestra principale come proprietaria
		wHall = new WHall(restTemplate);
		wHall.start("Visualizzazione Sale", hallStage);
		nHall = wHall.getTable();
		stage.setOnCloseRequest(event -> hallStage.close());//chiude la finestra delle sale quando si chiude la finestra principale
    }
	
	/** Imposta il layout */
    protected void setLayout() {
    	super.setLayout();
		grid.add(nHall, 2, 0, 4, 11);
    }

	/** Gestisce la logica delle sale*/
	private void impostaSala() {
		if (evento != null && dataF != null && salaF != null) {
			if (!Objects.equals(evento.getData(), dataF.getValue())) { //cambia la data
				liberaSala(evento.getSala(), evento.getData(), restTemplate);
				evento.setSala(null);
			}
			String oldSala = evento.getSala();
			String newSala = salaF.getValue();
			if (!Objects.equals(oldSala, newSala)) { //cambia la sala
				liberaSala(oldSala, evento.getData(), restTemplate);
				occupaSala(newSala, dataF.getValue(), restTemplate);
				evento.setSala(newSala);
			}
		}
	}
	
	/** Pulisce i campi della finestra*/
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
	
	/** Popola i campi della finestra*/
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
		salaF.setValue(evento.getSala() != null ? evento.getSala() : "");
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
		wHall.aggiornaTabella();
	}
	
	/** Popola l'evento con i dati della finestra*/
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
			this.evento.setSala(salaF.getValue() == null || salaF.getValue().equals("") ? null : salaF.getValue()); 
			this.evento.getElencoInterventi().clear();
			this.evento.addInterventi(interventiLista.getItems());
		}
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
			} catch (SystemException | HttpClientErrorException.NotFound ex) {
				msgF.setText("La creazione del nuovo evento non è riuscita.");
			}
		});
		
		// Gestisce la logica della ricerca di un evento
		cercaF.setPromptText("Inserisci l'ID dell'evento");
		cercaF.setOnAction(e -> {
			try {
				this.evento = getEvento(cercaF.getText(), restTemplate);
				populateFields(evento);
			} catch (SystemException | HttpClientErrorException.NotFound ex) {
				msgF.setText("Nessun evento trovato con l'ID specificato.");
				clearFields();
				idField.setText(cercaF.getText());
			}		    
		});

		// Gestisce la logica del cambio di data
		dataF.setOnAction(e -> {
			LocalDate newValue = dataF.getValue();
			if (newValue != null) {
				String[] saleOptions = getSaleLibere(newValue, restTemplate);
				salaF.setValue(null);
				salaF.setItems(FXCollections.observableArrayList(saleOptions));
			}
			impostaSala();
		});

		// Gestisce la logica del cambio di sala
		salaF.setOnAction(e -> impostaSala());

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
			} catch (SystemException | HttpClientErrorException.NotFound ex) {
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

	/** Gestisce la logica della navigazione degli eventi*/
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
	
}