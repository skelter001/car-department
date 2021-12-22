package com.griddynamics.cd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoColorWithSuchIdException extends RuntimeException {

    public NoColorWithSuchIdException(String msg) {
        super(msg);
    }

    public NoColorWithSuchIdException(long id) {
        super("Car with " + id + "id does not exist");
    }
}
