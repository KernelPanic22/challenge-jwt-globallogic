package com.GlobalLogic.security.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.GlobalLogic.security.model.DTO.LoginDTO;
import com.GlobalLogic.security.model.DTO.RegisterDTO;
import com.GlobalLogic.security.model.PlatformUserEntity;
import com.GlobalLogic.security.model.Token;
import com.GlobalLogic.security.model.exception.ValidationException;
import com.GlobalLogic.security.model.request.RegisterRequest;
import com.GlobalLogic.security.repository.UserRepository;
import com.GlobalLogic.security.service.JwtService;
import java.util.Collection;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthenticationServiceImpl authenticationService;

  @Test
  void LOGIN_OK() {
    Authentication auth = new Authentication() {
      @Override
      public String getName() {
        return "test";
      }

      @Override
      public boolean isAuthenticated() {
        return true;
      }

      @Override
      public Object getPrincipal() {
        return null;
      }      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

      }

      @Override
      public Object getDetails() {
        return null;
      }

      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override
      public Object getCredentials() {
        return null;
      }
    };
    PlatformUserEntity user = PlatformUserEntity.builder().name("test").email("test")
        .password("test").token(Token.builder().tokenValue("test").build()).build();

    when(jwtService.getAuthentication()).thenReturn(auth);
    when(userRepository.findByEmail("test")).thenReturn(user);
    when(jwtService.createToken(user)).thenReturn("token");
    when(userRepository.save(user)).thenReturn(user);

    assertEquals(authenticationService.login().getStatusCodeValue(), 200);
    assertEquals(Objects.requireNonNull(authenticationService.login().getBody()).getClass(),
        LoginDTO.class);

  }

  @Test
  void LOGIN_NOT_OK() {

    Authentication authentication = new Authentication() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getDetails() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return null;
      }

      @Override
      public boolean isAuthenticated() {
        return false;
      }

      @Override
      public String getName() {
        return null;
      }      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

      }
    };

    when(jwtService.getAuthentication()).thenReturn(authentication);
    assertThrows(ValidationException.class, () -> authenticationService.login());
  }

  @Test
  void REGISTER_OK() {
    RegisterRequest userRequest = RegisterRequest.builder().name("Julio Gonzalez")
        .email("julio@testssw.cl").password("a2asfGfdfdf4").build();

    PlatformUserEntity user = new PlatformUserEntity(userRequest);

    doReturn(user).when(userRepository).save(any(PlatformUserEntity.class));
    doReturn("token").when(jwtService).createToken(any(PlatformUserEntity.class));
    ResponseEntity<?> register = authenticationService.register(userRequest);

    assertEquals(register.getStatusCodeValue(), 200);
    assertEquals(Objects.requireNonNull(register.getBody()).getClass(), RegisterDTO.class);
  }

  @Test
  void REGISTER_NOT_OK() {
    RegisterRequest userRequest = RegisterRequest.builder().name("Julio Gonzalez")
        .email("usuario@gmail.com").password("a2asfGfdfdf4").build();

    PlatformUserEntity user = new PlatformUserEntity(userRequest);

    doReturn(user).when(userRepository).findByEmail(any(String.class));
    assertThrows(ValidationException.class, () -> authenticationService.register(userRequest));


  }
}