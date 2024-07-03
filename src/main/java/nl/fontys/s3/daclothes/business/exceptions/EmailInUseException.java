package nl.fontys.s3.daclothes.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailInUseException extends ResponseStatusException {
    public EmailInUseException()  {
        super(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_IN_USE");
    }
}
