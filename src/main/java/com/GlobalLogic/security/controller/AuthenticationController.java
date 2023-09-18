package com.GlobalLogic.security.controller;


import com.GlobalLogic.security.model.request.RegisterRequest;
import com.GlobalLogic.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest user) {
    return authenticationService.register(user);
  }

  @GetMapping("/login")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> login() {

    return authenticationService.login();
  }
}
