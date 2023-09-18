package com.GlobalLogic.security.model.DTO;

import com.GlobalLogic.security.model.Phone;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneDTO implements Serializable {

  private static final long serialVersionUID = 5926468583005150707L;

  private Integer number;

  private Integer cityCode;

  private Integer countryCode;

}
