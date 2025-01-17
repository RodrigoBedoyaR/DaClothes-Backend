package nl.fontys.s3.daclothes.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedDataAccessException extends ResponseStatusException {
    public UnauthorizedDataAccessException ( String errorCause ) {
        super(HttpStatus.FORBIDDEN, errorCause);
    }
}