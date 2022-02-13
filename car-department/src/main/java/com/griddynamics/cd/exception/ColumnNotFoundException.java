package com.griddynamics.cd.exception;

public class ColumnNotFoundException extends RuntimeException {

    public ColumnNotFoundException(String columnName) {
        super("Column " + columnName + " was not found");
    }
}
