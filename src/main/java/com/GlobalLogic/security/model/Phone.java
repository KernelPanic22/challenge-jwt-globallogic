package com.GlobalLogic.security.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity
public class Phone implements Serializable {

  private static final long serialVersionUID = 5926468583005150707L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id",insertable = false, updatable = false)
  @JsonBackReference
  private PlatformUserEntity platformUser;

  @JsonProperty("number")
  private Long number;

  @JsonProperty("citycode")
  private Integer cityCode;

  @JsonProperty("countrycode")
  private String countryCode;
}
