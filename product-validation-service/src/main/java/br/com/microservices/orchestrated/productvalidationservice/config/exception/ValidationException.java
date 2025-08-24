package br.com.microservices.orchestrated.productvalidationservice.config.exception;

import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
    public ValidationException (String message) {
        super(message);
    }
}
