package com.griddynamics.cd.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionAdviser extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Failed to find expected entity", ex);
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now(), null);
    }

    @ExceptionHandler(EntityDeleteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleEntityDeleteException(EntityDeleteException ex) {
        log.error("Failed delete entity");

        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now(), null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handle(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        String errorMessage;
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" ").append(violation.getMessage()));
            errorMessage = builder.toString();
        } else {
            errorMessage = "ConstraintViolationException occurred.";
        }
        return buildErrorResponse(errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now(), null);
    }

    @ExceptionHandler(ColumnNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleColumnNotFoundException(ColumnNotFoundException ex) {
        log.error("Failed finding column {}", ex.getMessage());

        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now(), null);
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleEntityExistsException(EntityExistsException ex) {
        log.error("Failed to parse request");

        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now(), null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        log.error("Failed to parse request");

        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(val -> new ErrorResponse.ValidationError(val.getField(), val.getDefaultMessage()))
                .collect(Collectors.toList());

        String[] msg = ex.getMessage().split("\\s+");

        return buildErrorResponse(msg[0] + ' ' + msg[1], HttpStatus.BAD_REQUEST, LocalDateTime.now(), validationErrors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Failed to parse json");

        String[] msg = Objects.requireNonNull(ex.getMessage()).split("\\s+");

        return buildErrorResponse(msg[0] + ' ' + msg[1] + ' ' + msg[2], HttpStatus.BAD_REQUEST, LocalDateTime.now(), null);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<Object> handleTooManyRequestException(HttpClientErrorException hcee) {
        log.error("Too many requests exception was caused", hcee);
        return buildErrorResponse(hcee.getMessage(), HttpStatus.TOO_MANY_REQUESTS, LocalDateTime.now(), null);
    }

    private ResponseEntity<Object> buildErrorResponse(String msg,
                                                      HttpStatus httpStatus,
                                                      LocalDateTime timestamp,
                                                      List<ErrorResponse.ValidationError> validationErrors) {
        ErrorResponse errorResponse = new ErrorResponse(timestamp, httpStatus.value(), msg, validationErrors);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
