package com.multipolar.sumsel.kasda.kasdagateway.controller;

import com.multipolar.sumsel.kasda.kasdagateway.converter.InvalidMessageException;
import org.jpos.iso.ISOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class ErrorControllerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, ISOException.class, InvalidMessageException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        Map<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("message", ex.getLocalizedMessage());
        errorInfo.put("status", HttpStatus.BAD_REQUEST);
        errorInfo.put("status_code", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConnectException.class})
    public ResponseEntity<Map<String, Object>> handleExceptionIsoException(Exception ex) {
        Map<String, Object> errorInfo = new HashMap<>();
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        errorInfo.put("message", ex.getLocalizedMessage());
        errorInfo.put("status", status);
        errorInfo.put("status_code", status.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_GATEWAY);
    }
}
