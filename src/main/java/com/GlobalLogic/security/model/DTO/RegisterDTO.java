package com.GlobalLogic.security.model.DTO;

import com.GlobalLogic.security.model.Token;
import java.util.UUID;
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
public class RegisterDTO {

  public UUID id;

  public String created;

  public String lastLogin;

  public String token;

  public Boolean isActive;
}
