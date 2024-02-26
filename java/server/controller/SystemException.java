package server.controller;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Classe di eccezione personalizzata che estende RuntimeException.
 * Questa eccezione viene lanciata quando una risorsa richiesta non viene trovata,
 * Spring risponde automaticamente con uno status HTTP 404 (Not Found).
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SystemException (String id) {
        super(id);
    }
}