package com.GlobalLogic.security.controller;

import com.GlobalLogic.security.model.request.RegisterRequest;
import com.GlobalLogic.security.service.AuthenticationService;
import com.GlobalLogic.security.model.exception.ValidationException;
import com.GlobalLogic.security.utils.ResponseEntityUtils;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest user) {
    try {
      return authenticationService.register(user);
    } catch (ValidationException ex) {
     return ResponseEntityUtils.ReponseError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
  }
  @GetMapping("/login")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> login() {
    try{
      return authenticationService.login();
    }catch (Exception ex){
      return ResponseEntityUtils.ReponseError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
  }
}
