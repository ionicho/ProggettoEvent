package client.windows;

import javafx.collections.*;
import javafx.scene.control.*;
import org.springframework.web.client.RestTemplate;
import java.util.function.Consumer;
import java.util.*;
import server.model.*;

/**
 * Classe generica per la gestione della finestra con l'elenco delle risorse e la loro disponibilità.
 * L'attributo {@link WResource#restTemplate} è l'istanza di {@link RestTemplate} utilizzata con
 * L'attributo {@link WResource#restHandler} per effettuare le richieste REST al server che
 * risponde al path {@link WResource#url}.
 * L'attributo {@link WResource#clickHandler} è l'istanza di {@link WResourceClick} utilizzata per
 * gestire gli eventi di click sulle celle della tabella.
 * @param <T> tipo generico che estende {@link server.model.Resource}
 */

public class WResource<T extends Resource> {

    protected WResourceClick<T> clickHandler;
    protected WResourceRest<T> restHandler;
    protected WEvent wEvent = null; // usata solo da WHall
    protected RestTemplate restTemplate;
    protected TableView<T> table;
    protected String url;

    protected WResource(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restHandler = new WResourceRest<>(this,restTemplate);
        this.clickHandler = new WResourceClick<>(this);
        this.table = new TableView<>(); // Inizializza table
    }

    // Metodo per ottenere la tabella come un Node
    public TableView<T> getTable() {
        return table;
    }

    // Metodo per ottenere (una copia) dei dati della tabella
    public List<T> getDati() {
        return new ArrayList<>(table.getItems());
    }

    public WResourceRest <T> getRestHandler() {
        return restHandler;
    }

    public WEvent getWEvent() {
        return wEvent;
    }

    protected void addColonneStatiche() {
        String centrato = "CENTER";
        String aDestra = "CENTER-RIGHT";
        // Personalizza queste colonne statiche secondo le tue esigenze
        TableColumn<T, String> cameraCol = StaticCol.creaCol("Nome", "nome", centrato);
        table.getColumns().add(cameraCol);
        TableColumn<T, Double> costoCol = StaticCol.creaCol("Costo", "costo", aDestra);
        table.getColumns().add(costoCol);
    }

    protected void addColonneDinamiche(Set<String> uniqueDates, Consumer<TableColumn<T, String>> clickHandler) {
        // Converti il Set in una List
        List<String> dateList = new ArrayList<>(uniqueDates);
        // Ordina la lista
        Collections.sort(dateList);
        // Ora aggiungi le colonne in base all'ordine della lista
        for (String date : dateList) {
            TableColumn<T, String> column = new TableColumn<>(date);
            column.setCellValueFactory(new DinamicCol<>(date));
            clickHandler.accept(column);
            table.getColumns().add(column);
        }
    }

    protected void mettiDati() {
        T[] resources = restHandler.getDati();
        // Crea un insieme di date uniche
        Set<String> uniqueDates = new HashSet<>();
        if (resources != null) {
            for (T resource : resources) {
                for (StateDate stateDate : resource.getDisponibilita()) {
                    uniqueDates.add(stateDate.getData().toString());
                }
            }
        }
        // Aggiungi colonne dinamiche per le date e gli stati
        addColonneDinamiche(uniqueDates, clickHandler::configuraClick);
        // Popola la tabella con i dati ricevuti
        ObservableList<T> data = FXCollections.observableArrayList(resources);
        table.setItems(data);
    }

    public void refresh() {
        // Rimuovi tutte le righe e colonne esistenti
        table.getItems().clear();
        table.getColumns().clear();
        addColonneStatiche();
        mettiDati();
    }

}
