package com.example.resource_booking_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.lang.RuntimeException;
import java.util.Date;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex,WebRequest request){
        Error er=new Error(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<> (er, HttpStatus.NOT_FOUND);
    }

}