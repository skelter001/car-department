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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionAdviser extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException enfe) {
        log.error("Failed to find expected entity", enfe);
        return buildErrorResponse(enfe.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now(), null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException manve, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        log.error("Failed to parse request");

        List<ErrorResponse.ValidationError> validationErrors = manve.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(val -> new ErrorResponse.ValidationError(val.getField(), val.getDefaultMessage()))
                .collect(Collectors.toList());

        String[] msg = manve.getMessage().split("\\s+");

        return buildErrorResponse(msg[0] + ' ' + msg[1], HttpStatus.BAD_REQUEST, LocalDateTime.now(), validationErrors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException hmnre, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Failed to parse json");

        String[] msg = Objects.requireNonNull(hmnre.getMessage()).split("\\s+");

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
