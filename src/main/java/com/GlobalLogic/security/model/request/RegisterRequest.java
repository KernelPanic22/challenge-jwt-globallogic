package com.GlobalLogic.security.model.request;

import com.GlobalLogic.security.model.Phone;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterRequest {

  private String name;

  private String email;

  private String password;

  @JsonProperty("phones")
  private List<Phone> phones;

}
