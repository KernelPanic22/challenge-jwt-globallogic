package com.GlobalLogic.security.config;


import com.GlobalLogic.security.model.util.Role;
import com.GlobalLogic.security.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final JwtAuthenticatorFilter jwtAuthFilter;

  private final JwtService jwtService;

  private final AuthenticationProvider authenticationProvider;

  private final AuthEntryPointJwt unauthorizedHandler;

  private final UserDetailsService userDetailsService;

  @Value("${SECRET_KEY}")
  private String SECRET_KEY;

  public SecurityConfiguration(JwtAuthenticatorFilter jwtAuthFilter, JwtService jwtService,
      AuthenticationProvider authenticationProvider, AuthEntryPointJwt unauthorizedHandler,
      UserDetailsService userDetailsService) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.jwtService = jwtService;
    this.authenticationProvider = authenticationProvider;
    this.unauthorizedHandler = unauthorizedHandler;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeHttpRequests().antMatchers("/api/v1/auth/login")
        .hasAuthority(Role.USER.name()).anyRequest().authenticated().and()
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().antMatchers("/api/v1/auth/register", "/h2-console/**");
  }
}
