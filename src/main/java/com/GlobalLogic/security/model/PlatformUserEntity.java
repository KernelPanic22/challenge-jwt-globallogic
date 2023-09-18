package com.GlobalLogic.security.model;

import com.GlobalLogic.security.model.request.RegisterRequest;
import com.GlobalLogic.security.model.util.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "platform_user")
public class PlatformUserEntity implements Serializable, UserDetails {

  private static final long serialVersionUID = 5926468583005150707L;

  @Id
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", name = "id")
  private UUID id;

  private String name;
  private String email;
  private String password;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  @JsonManagedReference
  private List<Phone> phones;


  private String role;

  private String createdAt;

  private String lastLogin;

  private Boolean isActive;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @JsonManagedReference
  private Token token;

  public PlatformUserEntity(RegisterRequest registerRequest) {
    this.name = registerRequest.getName();
    this.email = registerRequest.getEmail();
    this.password = registerRequest.getPassword();
    this.phones = registerRequest.getPhones();
    this.role = Role.USER.name();
    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
    this.createdAt = dateFormat.format(new Date(System.currentTimeMillis()));
    this.lastLogin = null;
    this.isActive = Boolean.TRUE;
    this.token = null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(this.role));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static PlatformUserEntity build(UserDetails userDetails) {
    return PlatformUserEntity.builder()
        .email(userDetails.getUsername())
        .password(userDetails.getPassword())
        .role(Role.USER.name())
        .build();
  }
}
