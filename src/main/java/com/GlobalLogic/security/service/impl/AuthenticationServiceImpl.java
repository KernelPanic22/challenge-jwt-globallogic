package com.GlobalLogic.security.service.impl;

import com.GlobalLogic.security.model.DTO.ErrorDTO;
import com.GlobalLogic.security.model.DTO.LoginDTO;
import com.GlobalLogic.security.model.DTO.PhoneDTO;
import com.GlobalLogic.security.model.DTO.RegisterDTO;
import com.GlobalLogic.security.model.PlatformUserEntity;
import com.GlobalLogic.security.model.Token;
import com.GlobalLogic.security.model.request.RegisterRequest;
import com.GlobalLogic.security.repository.UserRepository;
import com.GlobalLogic.security.service.AuthenticationService;
import com.GlobalLogic.security.service.JwtService;
import com.GlobalLogic.security.utils.DateUtils;
import java.sql.Date;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;

  private final JwtService jwtService;

  @Override
  public ResponseEntity<?> login() {

    Authentication userAuth = jwtService.getAuthentication();

    if (!userAuth.isAuthenticated()) {
      return ResponseEntity.badRequest().body(
          ErrorDTO.builder().message("Invalid token.").code(HttpStatus.BAD_REQUEST.value())
              .timestamp(DateUtils.formatDate(new Date(System.currentTimeMillis()),
                  "MMM dd, yyyy hh:mm:ss a")).build());
    }

    PlatformUserEntity user = userRepository.findByEmail(userAuth.getName());
    user.setLastLogin(
        DateUtils.formatDate(new Date(System.currentTimeMillis()), "MMM dd, yyyy hh:mm:ss a"));
    user.getToken().setTokenValue(jwtService.createToken(user));
    userRepository.save(user);

    return ResponseEntity.ok(LoginDTO.builder().id(user.getId()).created(user.getCreatedAt())
        .lastLogin(user.getLastLogin())
        .token(user.getToken().getTokenValue())
        .isActive(user.getIsActive())
        .name(user.getName()).email(user.getEmail())
        .password(user.getPassword())
        .phones(user.getPhones().stream().map(
            phone -> PhoneDTO.builder().number(phone.getNumber()).cityCode(phone.getCityCode())
                .countryCode(phone.getCountryCode()).build()).collect(Collectors.toList()))
        .build());
  }

  @Override
  public ResponseEntity<?> register(RegisterRequest user) {

    if (!Pattern.compile("^(?=[^A-Z]*[A-Z])(?=(?:[^0-9]*[0-9]){2})[a-zA-Z0-9]{8,12}$")
        .matcher(user.getPassword()).matches()) {
      ResponseEntity.badRequest().body("");
      return ResponseEntity.badRequest().body(ErrorDTO.builder().message(
              "The password must have at least 8 characters and maximum of 12 characters.With least 2 numbers and 1 uppercase letter.")
          .code(400).timestamp(
              DateUtils.formatDate(new Date(System.currentTimeMillis()), "MMM dd, yyyy hh:mm:ss a"))
          .build());
    }

    if (!Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(user.getEmail()).matches()) {
      return ResponseEntity.badRequest().body(
          ErrorDTO.builder().message("Invalid email format.").code(HttpStatus.BAD_REQUEST.value())
              .timestamp(DateUtils.formatDate(new Date(System.currentTimeMillis()),
                  "MMM dd, yyyy hh:mm:ss a")).build());
    }

    if (Objects.nonNull(userRepository.findByEmail(user.getEmail()))) {
      return ResponseEntity.badRequest().body(
          ErrorDTO.builder().message("Email already exists").code(HttpStatus.BAD_REQUEST.value())
              .timestamp(DateUtils.formatDate(new Date(System.currentTimeMillis()),
                  "MMM dd, yyyy hh:mm:ss a")).build());
    }

    PlatformUserEntity registeredUser = new PlatformUserEntity(user);
    registeredUser.setToken(
        Token.builder().user(registeredUser).tokenValue(jwtService.createToken(registeredUser))
            .build());

    userRepository.save(registeredUser);

    return ResponseEntity.ok(
        RegisterDTO.builder().id(registeredUser.getId()).created(registeredUser.getCreatedAt())
            .lastLogin(registeredUser.getLastLogin())
            .token(registeredUser.getToken().getTokenValue()).isActive(registeredUser.getIsActive())
            .build());
  }
}
