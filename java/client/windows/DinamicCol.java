package client.windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import server.model.*;

/**
 * Classe per l'aggiunta di colonne dinamiche (ovvero che il nome della colonna
 * è ricavato da un dato) ad una tabella
 */

public class DinamicCol implements Callback<CellDataFeatures<Room, String>, ObservableValue<String>> {
    private String date;

    public DinamicCol(String date) {
        this.date = date;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<Room, String> cellData) {
        Room currentRoom = cellData.getValue();
        for (StateDate sd : currentRoom.getDisponibilita()) {
            if (sd.getData().toString().equals(date)) {
                return new SimpleStringProperty(sd.getStato().toString());
            }
        }
        return new SimpleStringProperty("");
    }

}