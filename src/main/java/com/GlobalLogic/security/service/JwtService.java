package com.GlobalLogic.security.service;

import com.GlobalLogic.security.model.PlatformUserEntity;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

  String extractToken();

  String createToken(Map<String, Object> extraClaims, PlatformUserEntity userDetails);

  String createToken(PlatformUserEntity userDetails);

  Boolean validateToken(String token, UserDetails userDetails);

  UsernamePasswordAuthenticationToken authentication(String token,
      @NonNull HttpServletRequest request);

  Authentication getAuthentication();

  boolean validateToken(String token);

  String refreshToken(String token);

  String extractUserName(String token);


}
