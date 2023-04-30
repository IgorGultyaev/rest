package org.example.rest.exception;


public class AppNotFoundException extends AppException {
    public AppNotFoundException() {
    }


    public AppNotFoundException(String message) {
        super(message);
    }


    public AppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


    public AppNotFoundException(Throwable cause) {
        super(cause);
    }


    protected AppNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}


