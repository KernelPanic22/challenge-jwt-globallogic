package com.GlobalLogic.security.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "error")
public class ErrorDTO {

  @JsonProperty("timestamp")
  private String timestamp;

  @JsonProperty("codigo")
  private Integer code;

  @JsonProperty("detail")
  private String message;

}
