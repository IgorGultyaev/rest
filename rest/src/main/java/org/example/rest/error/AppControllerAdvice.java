package org.example.rest.error;


import org.example.rest.exception.AppException;
import org.example.rest.exception.AppNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.validation.ConstraintViolationException;
import java.util.Map;


@RestControllerAdvice
public class AppControllerAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRS handle(AppException e) {
        return new ErrorRS("err.app", Map.of());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorRS handle(AppNotFoundException e) {
        return new ErrorRS("err.item_not_found", Map.of());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRS handle(MethodArgumentNotValidException e) {
        return new ErrorRS("err.req_not_valid", Map.of());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorRS handle(ConstraintViolationException e) {
        return new ErrorRS("err.req_const_not_valid", Map.of());
    }
}


