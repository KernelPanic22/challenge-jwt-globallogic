package com.GlobalLogic.security.repository;

import com.GlobalLogic.security.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

  Token findByTokenValue(String token);
}
