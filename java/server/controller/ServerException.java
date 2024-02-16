package server.controller;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServerException extends RuntimeException {
    public ServerException (String id) {
        super(id);
    }
}
