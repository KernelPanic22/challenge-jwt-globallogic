package com.GlobalLogic.security.service;

import com.GlobalLogic.security.model.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

  ResponseEntity<?> login();

  ResponseEntity<?> register(RegisterRequest user);
}
