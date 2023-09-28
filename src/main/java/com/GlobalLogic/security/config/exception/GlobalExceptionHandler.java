package com.GlobalLogic.security.config.exception;

import com.GlobalLogic.security.model.DTO.ErrorDTO;
import com.GlobalLogic.security.model.exception.ValidationException;
import com.GlobalLogic.security.utils.ResponseEntityUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .reduce((a, b) -> a + ", " + b).orElse(ex.getMessage());
    return ResponseEntityUtils.ReponseError(message, HttpStatus.BAD_REQUEST.value());
  }

  @ExceptionHandler(value = {ValidationException.class})
  public ResponseEntity<ErrorDTO> handleValidationException(ValidationException ex) {
    return ResponseEntityUtils.ReponseError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
  }
}

