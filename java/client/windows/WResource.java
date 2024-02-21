package client.windows;

import javafx.collections.*;
import javafx.scene.control.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import server.model.*;

/**
 * Classe generica per la gestione della finestra con l'elenco delle risorse e la loro disponibilità.
 * Con la classe StdHandler consente di inviare una request al server e quando
 * riceve la notifica del tutto ok, aggiorna la videata.
 * @param <T> Tipo della risorsa
 */
public abstract class WResource<T extends Resource> {

    protected TableView<T> table;
    protected RestTemplate restTemplate;
    protected StdHandler<T> stdHandler;
    protected String url;

    protected WResource(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.table = new TableView<>(); // Inizializza table
    }

    // Metodo per ottenere la tabella come un Node
    public TableView<T> getTable() {
        return table;
    }

    public void refresh() {
        aggiornaTabella();
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

    protected void addColonneDinamiche(Set<String> uniqueDates) {
        // Converti il Set in una List
        List<String> dateList = new ArrayList<>(uniqueDates);
        // Ordina la lista
        Collections.sort(dateList);
        // Ora aggiungi le colonne in base all'ordine della lista
        for (String date : dateList) {
            TableColumn<T, String> column = new TableColumn<>(date);
            column.setCellValueFactory(new DinamicCol<>(date));
            column.setCellFactory(col -> stdHandler.creaCellaColorate());
            table.getColumns().add(column);
        }
    }

    protected abstract T[] getDati();

    protected void mettiDati() {
        T[] resources = getDati();
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
        addColonneDinamiche(uniqueDates);
        // Popola la tabella con i dati ricevuti
        ObservableList<T> data = FXCollections.observableArrayList(resources);
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

