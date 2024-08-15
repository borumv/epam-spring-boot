package com.example.workloadservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(TrainerWorkloadNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<TrainerWorkloadNotFoundException> customHandleNotFound(Exception ex) {

        logger.error("{}", ex.getCause());
        return new ResponseEntity<>( new TrainerWorkloadNotFoundException(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
