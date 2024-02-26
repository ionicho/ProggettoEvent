package client.windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import server.model.*;

/**
 * Classe che implementa l'interfaccia {@link javafx.util.Callback} per
 * la gestione di colonne dinamiche in una tabella. La classe è utilizzata
 * per la visualizzazione della disponibilità delle risorse in una tabella. * 
 * @param <T> tipo generico che estende {@link server.model.Resource}
 */

public class DinamicCol<T extends Resource> implements Callback<CellDataFeatures<T, String>, ObservableValue<String>> {
    private String date;

    public DinamicCol(String date) {
        this.date = date;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<T, String> cellData) {
        T currentResource = cellData.getValue();
        for (StateDate sd : currentResource.getDisponibilita()) {
            if (sd.getData().toString().equals(date)) {
                return new SimpleStringProperty(sd.getStato().toString());
            }
        }
        return new SimpleStringProperty("");
    }

}