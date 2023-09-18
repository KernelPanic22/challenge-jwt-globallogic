package com.GlobalLogic.security.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.GlobalLogic.security.model.PlatformUserEntity;
import com.GlobalLogic.security.model.Token;
import com.GlobalLogic.security.model.util.Role;
import com.GlobalLogic.security.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;


@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

  @InjectMocks
  private JwtServiceImpl jwtService;

  @Mock
  private TokenRepository tokenRepository;

  @Mock
  private UserDetailsService userDetailsService;

  private static final String SECRET_KEY = "3Xh6T5cjcDd9HbDcDspa1g7ftQioc2rAixbQcQxFhe/IOohB8K1767+HaLU96SBN";

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    jwtService = new JwtServiceImpl(tokenRepository,userDetailsService);
    jwtService.SECRET_KEY = SECRET_KEY;
  }

  @Test
  public void testCreateToken() {
    PlatformUserEntity userDetails = new PlatformUserEntity();
    userDetails.setEmail("test@example.com");

    String token = jwtService.createToken(userDetails);

    assertNotNull(token);
  }

  @Test
  public void testValidateTokenWithValidToken() {
    String token = createValidToken();

    UserDetails userDetails = createUserDetails();
    PlatformUserEntity user = PlatformUserEntity.build(userDetails);
    when(tokenRepository.findByTokenValue(token)).thenReturn(Token.builder().id(Long.valueOf(1)).tokenValue(token).user(user).build());

    assertTrue(jwtService.validateToken(token, userDetails));
  }

  @Test
  public void testValidateTokenWithExpiredToken() {
    String expiredToken = createExpiredToken();

    UserDetails userDetails = createUserDetails();
    when(tokenRepository.findByTokenValue(expiredToken)).thenReturn(new Token());

    assertFalse(jwtService.validateToken(expiredToken, userDetails));
  }

  @Test
  public void testValidateTokenWithInvalidToken() {
    String invalidToken = "invalid-token";

    UserDetails userDetails = createUserDetails();
    when(tokenRepository.findByTokenValue(invalidToken)).thenReturn(null);

    assertFalse(jwtService.validateToken(invalidToken, userDetails));
  }

  @Test
  public void testExtractUserName() {
    String token = createValidToken();

    String username = jwtService.extractUserName(token);

    assertEquals("test@example.com", username);
  }

  @Test
  public void testGetAuthentication() {
    Authentication authentication = jwtService.getAuthentication();

    assertNull(authentication);
  }

  @Test
  @WithMockUser(username = "test@example.com")
  public void testAuthentication() {
    String token = createValidToken();
    UserDetails userDetails = createUserDetails();
    when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);

    MockHttpServletRequest request =  // You need to mock HttpServletRequest if needed
        new MockHttpServletRequest("GET", "/api/v1");
    request.addHeader("Authorization", "Bearer " + token);

    Authentication authentication = jwtService.authentication(token, request);

    assertNotNull(authentication);
    assertEquals("test@example.com", authentication.getName());
  }

  private String createValidToken() {
    PlatformUserEntity userDetails = new PlatformUserEntity();
    userDetails.setEmail("test@example.com");

    return jwtService.createToken(userDetails);
  }

  private String createExpiredToken() {
    return Jwts.builder()
        .setSubject("test@example.com")
        .setIssuedAt(new Date(System.currentTimeMillis() - 1000)) // Expired token
        .setExpiration(new Date(System.currentTimeMillis()))
        .signWith(getSignInKey())
        .compact();
  }

  private UserDetails createUserDetails() {
    Collection<? extends GrantedAuthority> roles = AuthorityUtils
        .createAuthorityList(Role.USER.name());
    return new User("test@example.com", "password", roles);
  }

  private Key getSignInKey() {
    byte[] kBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(kBytes);
  }
}