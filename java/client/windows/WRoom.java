package client.windows;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;
import server.AppConfig;
import server.model.*;

public class WRoom extends WResource<Room> {
    private Scene scene;

    public WRoom(RestTemplate restTemplate) {
        super(restTemplate);
        this.url = AppConfig.getURL() + "api/room";
        this.wResourceClick = new WResourceClick<>(this, restTemplate);
        addColonneStatiche();
        mettiDati();
        this.scene = new Scene(table, 1200, 400);
    }

    public void start(String title, Stage primaryStage) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @SuppressWarnings({ "null" })
    @Override
    protected Room[] getDati() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Room[].class);
    }

    @Override
    protected void addColonneStatiche() {
        super.addColonneStatiche();
        String centrato = "CENTER";
        TableColumn<Room, RoomType> tipoCol = StaticCol.creaCol("Tipo", "tipo", centrato);
        table.getColumns().add(tipoCol);
        TableColumn<Room, Integer> lettiCol = StaticCol.creaCol("N. Letti", "numeroLetti", centrato);
        table.getColumns().add(lettiCol);
    }

}