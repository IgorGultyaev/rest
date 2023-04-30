package org.example.rest.exception;


public class AppForbiddenException extends AppException {
    public AppForbiddenException() {
        super();
    }


    public AppForbiddenException(String message) {
        super(message);
    }


    public AppForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }


    public AppForbiddenException(Throwable cause) {
        super(cause);
    }


    protected AppForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}


