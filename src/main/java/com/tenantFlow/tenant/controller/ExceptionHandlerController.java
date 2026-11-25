package com.tenantFlow.tenant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<String>> handleBindException(BindException ex) {
        List<String> errors = ex.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
