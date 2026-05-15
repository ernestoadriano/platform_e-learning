package dev.elearing.platform.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    public RestErrorMessage runtimeHandler(RuntimeException exception) {
        return new RestErrorMessage(exception.getMessage());
    }
}
