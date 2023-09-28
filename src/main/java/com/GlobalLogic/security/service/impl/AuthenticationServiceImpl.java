package com.GlobalLogic.security.service.impl;

import com.GlobalLogic.security.model.DTO.LoginDTO;
import com.GlobalLogic.security.model.DTO.PhoneDTO;
import com.GlobalLogic.security.model.DTO.RegisterDTO;
import com.GlobalLogic.security.model.PlatformUserEntity;
import com.GlobalLogic.security.model.Token;
import com.GlobalLogic.security.model.exception.ValidationException;
import com.GlobalLogic.security.model.request.RegisterRequest;
import com.GlobalLogic.security.repository.UserRepository;
import com.GlobalLogic.security.service.AuthenticationService;
import com.GlobalLogic.security.service.JwtService;
import com.GlobalLogic.security.utils.DateUtils;
import java.sql.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;

  private final JwtService jwtService;

  public AuthenticationServiceImpl(UserRepository userRepository, JwtService jwtService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  @Override
  public ResponseEntity<?> login() throws ValidationException {

    Authentication userAuth = jwtService.getAuthentication();

    if (!userAuth.isAuthenticated()) {
      throw new ValidationException("Invalid token");
    }

    PlatformUserEntity user = userRepository.findByEmail(userAuth.getName());
    user.setLastLogin(
        DateUtils.formatDate(new Date(System.currentTimeMillis()), "MMM dd, yyyy hh:mm:ss a"));
    user.getToken().setTokenValue(jwtService.createToken(user));
    userRepository.save(user);

    return ResponseEntity.ok(LoginDTO.builder().id(user.getId()).created(user.getCreatedAt())
        .lastLogin(user.getLastLogin()).token(user.getToken().getTokenValue())
        .isActive(user.getIsActive()).name(user.getName()).email(user.getEmail())
        .password(user.getPassword()).phones(
            Objects.nonNull(user.getPhones()) ? user.getPhones().stream().map(
                phone -> PhoneDTO.builder().number(phone.getNumber()).cityCode(phone.getCityCode())
                    .countryCode(phone.getCountryCode()).build()).collect(Collectors.toList())
                : null).build());
  }

  @Override
  public ResponseEntity<?> register(RegisterRequest user) throws ValidationException {
    if (!Objects.isNull(userRepository.findByEmail(user.getEmail()))) {
      throw new ValidationException("E-mail already exists");
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
