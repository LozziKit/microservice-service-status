package io.lozzikit.servicestatus.api.exceptions.handler;

import io.lozzikit.servicestatus.api.dto.ApiError;
import io.lozzikit.servicestatus.api.dto.ApiValidationError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ArgumentNotValidExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ApiValidationError> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((e) -> {
            ApiValidationError error = new ApiValidationError();
            error.setField(((DefaultMessageSourceResolvable) e.getArguments()[0]).getCodes()[1]);
            error.setError(e.getDefaultMessage());
            error.setRejectedValue(((FieldError) e).getRejectedValue());
            errors.add(error);
        });
        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
