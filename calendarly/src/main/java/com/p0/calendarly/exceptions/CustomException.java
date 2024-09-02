package com.p0.calendarly.exceptions;


import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomException extends RuntimeException {

    CustomException() {}
    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause){
        super(message, cause);
    }
}
