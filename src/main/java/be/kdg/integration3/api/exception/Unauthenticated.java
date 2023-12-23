package be.kdg.integration3.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class Unauthenticated extends RuntimeException{
    public Unauthenticated() {
        super("User is not authenticated");
    }
}
