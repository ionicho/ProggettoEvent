package client.windows;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Classe per l'aggiunta di colonne statiiche (ovvero che il nome della colonna
 * è predefinito) ad una tabella
 */

public class StaticCol {
	
	private StaticCol() { //tanto il metodo è statico
		super();
	}

    public static <S,T> TableColumn<S,T> creaCol(String title, String property, String alignment) {
        TableColumn<S,T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setStyle("-fx-alignment: " + alignment);
        return col;
    }
}


