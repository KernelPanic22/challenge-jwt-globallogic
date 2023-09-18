package com.GlobalLogic.security.service.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TokenParseException extends RuntimeException {

    public TokenParseException(String message) {
      super(message);
    }

}
