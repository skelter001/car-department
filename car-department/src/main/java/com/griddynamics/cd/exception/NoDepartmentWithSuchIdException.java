package com.griddynamics.cd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDepartmentWithSuchIdException extends RuntimeException {

    public NoDepartmentWithSuchIdException(String msg) {
        super(msg);
    }

    public NoDepartmentWithSuchIdException(long id) {
        super("Department with " + id + "id does not exist");
    }
}
