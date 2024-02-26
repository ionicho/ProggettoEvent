package server.controller;

import org.springframework.context.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * La classe SystemController è un componente Spring che funge da controller.
 * Gestisce le richieste REST per l'arresto del server. Quando riceve una richiesta
 * per l'endpoint "api/arresta-server", chiude l'applicazione.
 */

@RestController
public class SystemController {

    private final ApplicationContext appContext;

    public SystemController(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @DeleteMapping("api/arresta-server")
    public ResponseEntity<String> arrestaServer() {
        ((ConfigurableApplicationContext) appContext).close(); // Chiude l'applicazione
        return new ResponseEntity<>("Richiesta di arresto ricevuta", HttpStatus.OK);
    }
}
