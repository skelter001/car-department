package com.griddynamics.cd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoEmployeesWithSuchIdException extends RuntimeException {

    public NoEmployeesWithSuchIdException(String msg) {
        super(msg);
    }

    public NoEmployeesWithSuchIdException(long id) {
        super("Employee with " + id + "id does not exist");
    }
}
