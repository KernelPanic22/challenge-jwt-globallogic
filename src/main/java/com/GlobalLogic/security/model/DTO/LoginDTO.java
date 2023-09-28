package com.GlobalLogic.security.model.DTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {

  private static final long serialVersionUID = 5926468583005150707L;

  private UUID id;

  private String created;

  private String lastLogin;

  private String token;

  private Boolean isActive;

  private String name;

  private String email;

  private String password;

  private List<PhoneDTO> phones;

}
