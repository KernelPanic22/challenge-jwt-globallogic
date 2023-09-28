package com.GlobalLogic.security.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenParseException extends RuntimeException {

    public TokenParseException(String message) {
      super(message);
    }

}
