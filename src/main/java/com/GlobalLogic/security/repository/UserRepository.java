package com.GlobalLogic.security.repository;

import com.GlobalLogic.security.model.PlatformUserEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PlatformUserEntity, UUID> {

  PlatformUserEntity findByEmail(String email);
}
