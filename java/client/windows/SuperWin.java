package client.windows;


import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class SuperWin {
	protected static final int WIDTH =150;
	
	public Button creaButton (String text) {
		Button button = new Button(text);
        GridPane.setHalignment(button, HPos.CENTER);
        button.setPrefWidth(WIDTH);
        return button;
	}
    
    public Label creaLabel(String text) {
        Label label = new Label(text);
        GridPane.setHgrow(label, Priority.ALWAYS);
        GridPane.setHalignment(label, HPos.RIGHT);
        label.setPrefWidth(WIDTH);
        return label;
    }
    
    public TextField creaTextField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        field.setText("");
        return field;
    }

    public ComboBox<String> creaComboBox(String[] options) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(WIDTH);
        comboBox.getItems().addAll(options);
        return comboBox;
    }
    
    public TextField creaIntegerField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        return field;
    }
    
    public TextField creaDoubleField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        field.setText("0.0");
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldValue);
            }
        });
        return field;
    }
    
    public TextField creaHourField() {
        TextField field = new TextField();
        field.setPrefWidth(WIDTH);
        field.setText("");
        field.setPromptText("HH:mm");
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("([01]?\\d|2[0-3])?(:[0-5]?\\d?)?") || newValue.length() > 5) {
                field.setText(oldValue);
            }
        });
        return field;
    }

}