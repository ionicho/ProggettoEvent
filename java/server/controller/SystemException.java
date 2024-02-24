package server.controller;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SystemException extends RuntimeException {
    public SystemException (String id) {
        super(id);
    }
}