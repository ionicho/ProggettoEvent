package client.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import server.model.*;

/**
 * Classe per la gestione della finestra di visualizzazione delle sale.
 * La classe estende {@link WResource} e ne implementa i metodi astratti.
 * La classe viene utilizzata per la visualizzazione delle sale,
 * in una sottofinestra di {@link WEvent}.
 * @param wEvent parametro della classe {@link WEvent}, utilizzato come
 * riferimento per la finestra principale.
 */

public class WHall extends WResource<Hall> {
    private Scene scene;

    public WHall(RestTemplate restTemplate) {
        this(restTemplate, null);
    }

    public WHall(RestTemplate restTemplate, WEvent wEvent) {
        super(restTemplate);
        this.wEvent = wEvent;
        addColonneStatiche();
        mettiDati();
        this.scene = new Scene(table, 400, 400);
    }

    public void start(String title, Stage primaryStage) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public WEvent getWEvent() {
        return wEvent;
    }

    @Override
    protected void addColonneStatiche() {
        super.addColonneStatiche();
        String centrato = "CENTER";
        TableColumn<Hall, Integer> postiCol = StaticCol.creaCol("N. Posti", "numeroPosti", centrato);
        table.getColumns().add(postiCol);
    }

}