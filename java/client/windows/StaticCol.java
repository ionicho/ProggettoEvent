package client.windows;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Il metodo statico per la creazione di colonne statiche in una tabella.
 * <S> tipo generico per la tabella (in genere Resource, Hall, ecc.)
 * <T> tipo generico per la colonna (in genere String, Integer, ecc.)
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


