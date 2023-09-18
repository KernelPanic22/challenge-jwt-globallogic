package com.GlobalLogic.security.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.GlobalLogic.security.model.PlatformUserEntity;
import com.GlobalLogic.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

class UserDetailsServiceImplTest {

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Mock
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testLoadUserByUsername() {
    // Mock a user entity for the repository
    PlatformUserEntity userEntity = new PlatformUserEntity();
    userEntity.setEmail("test@example.com");
    userEntity.setPassword("password"); // Set the password as needed

    // Mock the behavior of the UserRepository
    when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

    // Call the loadUserByUsername method
    UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

    assertNotNull(userDetails);
    assertEquals("test@example.com", userDetails.getUsername());
    // You can add more assertions for other user details if needed
  }
}