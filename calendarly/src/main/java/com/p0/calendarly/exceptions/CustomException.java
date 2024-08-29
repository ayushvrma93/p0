package com.p0.calendarly.exceptions;

public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }

    CustomException(String message, Throwable cause){
        super(message, cause);
    }
}
