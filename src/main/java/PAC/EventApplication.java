package PAC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import client.ClientApplication;
import javafx.application.Application;

@SpringBootApplication
public class EventApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventApplication.class, args);
		
     // Avvia il client dopo che il server è stato avviato
        Application.launch(ClientApplication.class, args);
	}

}
