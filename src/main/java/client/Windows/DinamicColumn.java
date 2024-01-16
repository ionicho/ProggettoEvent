package client.Windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import server.model.*;


public class DinamicColumn implements Callback<CellDataFeatures<ResourceRoom, String>, ObservableValue<String>> {
    private String date;

    public DinamicColumn(String date) {
        this.date = date;
    }

    @Override
    public ObservableValue<String> call(CellDataFeatures<ResourceRoom, String> cellData) {
        ResourceRoom currentRoom = cellData.getValue();
        for (StateDate sd : currentRoom.getDisponibilita()) {
            if (sd.getData().toString().equals(date)) {
                return new SimpleStringProperty(sd.getStato().toString());
            }
        }
        return new SimpleStringProperty("");
    }

}