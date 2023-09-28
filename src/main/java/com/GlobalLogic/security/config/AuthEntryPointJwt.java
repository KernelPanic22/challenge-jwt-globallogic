package com.GlobalLogic.security.config;

import com.GlobalLogic.security.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException, java.io.IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


    final Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", DateUtils.formatDate(new Date(System.currentTimeMillis()), "MMM dd, yyyy hh:mm:ss a"));
    body.put("codigo", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("detail", authException.getMessage());


    final Map<String, Object> errorDTO = new LinkedHashMap<>();
    errorDTO.put("error", body);

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), errorDTO);

  }

}
