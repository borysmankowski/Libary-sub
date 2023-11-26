package com.example.borys_mankowski_test_10.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleResourceNotFoundException(ResourceNotFoundException exception) {
        return createExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleDuplicateResourceException(DuplicateResourceException exception) {
        return createExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleDatabaseException(DatabaseException exception) {
        return createExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleIllegalArgumentException(IllegalArgumentException exception) {
        return createExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ExceptionDto handleEmailException(EmailException exception) {
        return createExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(UserEnablingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleUserEnablingException(UserEnablingException exception) {
        return createExceptionDto(exception.getMessage());
    }

    private ExceptionDto createExceptionDto(String message) {
        return new ExceptionDto(message);
    }

}
