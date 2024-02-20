package client.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import server.AppConfig;
import server.model.*;

public class WHall extends WResource<Hall> {
    private Scene scene;

    public WHall(RestTemplate restTemplate) {
        super(restTemplate);
        this.url = AppConfig.getURL() + "api/hall";
        this.stdHandler = new StdHandler<>(this, restTemplate);
        addColonneStatiche();
        mettiDati();
        this.scene = new Scene(table, 400, 400);
    }

    public void start(String title, Stage primaryStage) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @SuppressWarnings({ "null" })
    @Override
    protected Hall[] getDati() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Hall[].class);
    }

    @Override
    protected void addColonneStatiche() {
        super.addColonneStatiche();
        String centrato = "CENTER";
        TableColumn<Hall, Integer> postiCol = StaticCol.creaCol("N. Posti", "numeroPosti", centrato);
        table.getColumns().add(postiCol);
    }

}