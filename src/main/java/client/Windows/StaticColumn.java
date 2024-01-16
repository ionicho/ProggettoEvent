package client.Windows;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class StaticColumn {

    public static <S,T> TableColumn<S,T> createColumn(String title, String property, String alignment) {
        TableColumn<S,T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setStyle("-fx-alignment: " + alignment);
        return column;
    }
}

