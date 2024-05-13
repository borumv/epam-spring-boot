package com.vlasevsky.gym.handler;

import com.vlasevsky.gym.exceptions.ExceptionNotFoundResponse;
import com.vlasevsky.gym.exceptions.NotFoundException;
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


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionNotFoundResponse> customHandleNotFound(Exception ex) {

        ExceptionNotFoundResponse errors = new ExceptionNotFoundResponse(HttpStatus.NOT_FOUND.value(),
                "Not found exception",
                ex.getMessage(),
                ex.getMessage().split(" ")[0]);
        logger.error("{}", ex.getCause());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

}
