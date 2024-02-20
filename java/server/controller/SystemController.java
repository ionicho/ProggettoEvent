package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class SystemController {

    @Autowired
    private ApplicationContext appContext;

    @DeleteMapping("api/arresta-server")
    public ResponseEntity<String> arrestaServer() {
        ((ConfigurableApplicationContext) appContext).close(); // Chiude l'applicazione
        return new ResponseEntity<>("Richiesta di arresto ricevuta", HttpStatus.OK);
    }
}