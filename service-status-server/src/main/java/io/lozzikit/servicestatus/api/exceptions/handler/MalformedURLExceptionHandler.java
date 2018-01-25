package io.lozzikit.servicestatus.api.exceptions.handler;

import io.lozzikit.servicestatus.api.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.MalformedURLException;

@ControllerAdvice
public class MalformedURLExceptionHandler{

    @ExceptionHandler(MalformedURLException.class)
    ResponseEntity<ApiError> handleMalformedURLException(MalformedURLException ex) {
        ApiError error = new ApiError();
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
