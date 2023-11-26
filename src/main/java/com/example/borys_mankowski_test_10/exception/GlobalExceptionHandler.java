package com.example.borys_mankowski_test_10.exception;


import org.hibernate.exception.ConstraintViolationException;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        Throwable rootCause = exception.getMostSpecificCause();
        if (rootCause instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) rootCause;

            // Check if the violated constraint is related to the 'email' field
            if (constraintViolationException.getCause().getMessage().contains("uk_email")) {
                return new ExceptionDto("User with the given email already exists.");
            }

            // Add more conditions for other unique constraints in your application
        }
        return new ExceptionDto("A database error occurred. Please try again later.");
    }

    private ExceptionDto createExceptionDto(String message) {
        return new ExceptionDto(message);
    }

}
