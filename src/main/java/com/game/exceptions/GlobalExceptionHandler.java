package com.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> exceptionHandler (Exception exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (exception instanceof EntityNotFoundException)
            status = HttpStatus.NOT_FOUND;

        EmployeeIncorrectData someException = new EmployeeIncorrectData();
        someException.setMessage(exception.getMessage());

        return new ResponseEntity<>(someException, status);
    }
}
