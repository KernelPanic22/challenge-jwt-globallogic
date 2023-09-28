package com.GlobalLogic.security.utils;

import com.GlobalLogic.security.model.DTO.ErrorDTO;
import java.sql.Date;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtils {

  public static ResponseEntity<ErrorDTO> ReponseError(String message, Integer code){
    return ResponseEntity.status(code).body(createCustomErrorDTO(message, code));
    }
    public static ErrorDTO createCustomErrorDTO(String message, Integer code){
    return ErrorDTO.builder().message(message).code(code)
        .timestamp(DateUtils.formatDate(new Date(System.currentTimeMillis()),
            "MMM dd, yyyy hh:mm:ss a")).build();
  }

}
