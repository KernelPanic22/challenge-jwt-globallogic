package com.GlobalLogic.security.service.impl;

import com.GlobalLogic.security.model.PlatformUserEntity;
import com.GlobalLogic.security.model.Token;
import com.GlobalLogic.security.repository.TokenRepository;
import com.GlobalLogic.security.service.JwtService;
import com.GlobalLogic.security.service.impl.exception.TokenParseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  @Value("${SECRET_KEY}")
  String SECRET_KEY;

  private final TokenRepository tokenRepository;
  private final UserDetailsService userDetailsService;

  @Override
  public String extractToken() {
    return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
  }

  @Override
  public String createToken(Map<String, Object> extraClaims, PlatformUserEntity userDetails) {
    return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getEmail())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
  }

  @Override
  public String createToken(PlatformUserEntity userDetails) {
    return createToken(new HashMap<>(), userDetails);
  }

  @Override
  public Boolean validateToken(String token, UserDetails userDetails) {
    Token tokenEntity = tokenRepository.findByTokenValue(token);
    if ((Objects.isNull(tokenEntity) || Objects.isNull(tokenEntity.getId()))
        || !tokenEntity.getTokenValue().equals(token)) {
      return false;
    }
    return extractUserName(token).equals(userDetails.getUsername()) && !isTokenExpired(token)
        && tokenEntity.getUser().getEmail().equals(userDetails.getUsername());
  }

  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Override
  public UsernamePasswordAuthenticationToken authentication(String token,
      @NonNull HttpServletRequest request) {
    final UserDetails userDetails = userDetailsService.loadUserByUsername(extractUserName(token));

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());
    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    return authenticationToken;
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  @Override
  public boolean validateToken(String token) {
    return false;
  }

  @Override
  public String refreshToken(String token) {
    return Jwts.builder().setSubject(extractUserName(token))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(getSignInKey()).compact();
  }

  @Override
  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) throws TokenParseException{
    try{
    return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token)
        .getBody();
    }catch (Exception e){
      throw new TokenParseException("Invalid token");
    }
  }

  private Key getSignInKey() {
    byte[] kBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(kBytes);
  }

}
