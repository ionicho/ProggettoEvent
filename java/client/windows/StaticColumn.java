package client.windows;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Classe per l'aggiunta di colonne statiiche (ovvero che il nome della colonna
 * è predefinito) ad una tabella
 */

public class StaticColumn {
	
	private StaticColumn() { //tanto il metodo è statico
		super();
	}

    public static <S,T> TableColumn<S,T> createColumn(String title, String property, String alignment) {
        TableColumn<S,T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setStyle("-fx-alignment: " + alignment);
        return column;
    }
}

