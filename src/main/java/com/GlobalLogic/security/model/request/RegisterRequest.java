package com.GlobalLogic.security.model.request;

import com.GlobalLogic.security.model.Phone;
import com.GlobalLogic.security.model.exception.ValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterRequest {

  private String name;

  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format.")
  @NotEmpty(message = "Email is required.")
  private String email;

  @Pattern(regexp = "^(?=[^A-Z]*[A-Z])(?=(?:[^0-9]*[0-9]){2})[a-zA-Z0-9]{8,12}$", message = "Password must have 1 uppercase letter, 2 digits and 8 characters and maximum of 12 characters.")
  @NotEmpty(message = "Password is required.")
  private String password;

  @JsonProperty("phones")
  @Nullable
  private List<Phone> phones;

  public void validate() {
    if (!isValidEmail(email)) {
      throw new ValidationException("Invalid email format.");
    }

    if (!isValidPassword(password)) {
      throw new ValidationException("Password must have 1 uppercase letter, 2 digits, and 8-12 characters.");
    }
  }

  private boolean isValidEmail(String email) {
    // Implement your email validation logic here using the regex pattern.
    return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }

  private boolean isValidPassword(String password) {
    // Implement your password validation logic here using the regex pattern.
    return password.matches("^(?=[^A-Z]*[A-Z])(?=(?:[^0-9]*[0-9]){2})[a-zA-Z0-9]{8,12}$");
  }

}
