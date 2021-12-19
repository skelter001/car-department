package com.griddynamics.cd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoCarWithSuchIdException extends RuntimeException {

    public NoCarWithSuchIdException(String msg) {
        super(msg);
    }

    public NoCarWithSuchIdException(long id) {
        super("Car with " + id + "id does not exist");
    }
}
