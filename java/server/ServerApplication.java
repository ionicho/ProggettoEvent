package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import client.ReceptionistApplication;
import javafx.application.Application;

/**
 * Main del server, per comodità fa partire anche il main dei clients
 */

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		
     // Avvia il client dopo che il server è stato avviato
        Application.launch(ReceptionistApplication.class, args);

	}

}