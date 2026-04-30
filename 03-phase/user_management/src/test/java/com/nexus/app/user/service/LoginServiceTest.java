/*
Generate unit tests for LoginService using JUnit 5 and Mockito.

Requirements:
- Use @ExtendWith(MockitoExtension.class)
- Mock LoginRepository
- Test all methods:
    - createLogin (success and failure cases)
    - login (success and resource not found)
- Use given-when-then structure
- Verify repository interactions
- Use assertThrows for exceptions
- Achieve 100% coverage
- Do not use SpringBootTest
*/

package com.nexus.app.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexus.app.user.domain.dto.LoginCreateDTO;
import com.nexus.app.user.domain.dto.LoginRequestDTO;
import com.nexus.app.user.domain.dto.LoginResponseDTO;
import com.nexus.app.user.domain.projection.LoginProjection;
import com.nexus.app.user.exception.ResourceNotFoundException;
import com.nexus.app.user.repository.LoginRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService Unit Tests")
public class LoginServiceTest {

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private LoginService loginService;

    private LoginCreateDTO loginCreateDTO;
    private LoginRequestDTO loginRequestDTO;
    private LoginResponseDTO loginResponseDTO;
    private LoginProjection loginProjection;

    @BeforeEach
    void setUp() {
        // Given: Setup test data
        loginCreateDTO = new LoginCreateDTO(
            1,
            "testuser",
            "securePassword123"
        );

        loginRequestDTO = new LoginRequestDTO(
            "testuser",
            "securePassword123"
        );

        loginProjection = new LoginProjection() {
            @Override
            public Integer getUserId() {
                return 1;
            }

            @Override
            public String getName() {
                return "Test User";
            }

            @Override
            public LocalDateTime getLastLogin() {
                return LocalDateTime.of(2026, 4, 30, 10, 30);
            }
        };

        loginResponseDTO = new LoginResponseDTO(
            1,
            "Test User",
            LocalDateTime.of(2026, 4, 30, 10, 30)
        );
    }

    @Test
    @DisplayName("createLogin should return true when login is created successfully")
    void testCreateLogin_Success() {
        // Given: Repository successfully creates login
        when(loginRepository.createLogin(
            loginCreateDTO.userId(),
            loginCreateDTO.username(),
            loginCreateDTO.password()
        )).thenReturn(true);

        // When: Service createLogin is called
        Boolean result = loginService.createLogin(loginCreateDTO);

        // Then: Verify result and repository interaction
        assertNotNull(result);
        assertTrue(result);
        verify(loginRepository, times(1)).createLogin(
            loginCreateDTO.userId(),
            loginCreateDTO.username(),
            loginCreateDTO.password()
        );
    }

    @Test
    @DisplayName("createLogin should throw IllegalStateException when repository returns false")
    void testCreateLogin_RepositoryReturnsFalse() {
        // Given: Repository returns false
        when(loginRepository.createLogin(
            loginCreateDTO.userId(),
            loginCreateDTO.username(),
            loginCreateDTO.password()
        )).thenReturn(false);

        // When & Then: Service should throw IllegalStateException
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> loginService.createLogin(loginCreateDTO)
        );

        assertTrue(exception.getMessage().contains("Login could not be created"));
        verify(loginRepository, times(1)).createLogin(
            loginCreateDTO.userId(),
            loginCreateDTO.username(),
            loginCreateDTO.password()
        );
    }

    @Test
    @DisplayName("createLogin should throw IllegalStateException when repository returns null")
    void testCreateLogin_RepositoryReturnsNull() {
        // Given: Repository returns null
        when(loginRepository.createLogin(
            loginCreateDTO.userId(),
            loginCreateDTO.username(),
            loginCreateDTO.password()
        )).thenReturn(null);

        // When & Then: Service should throw IllegalStateException
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> loginService.createLogin(loginCreateDTO)
        );

        assertTrue(exception.getMessage().contains("Login could not be created"));
        verify(loginRepository, times(1)).createLogin(
            loginCreateDTO.userId(),
            loginCreateDTO.username(),
            loginCreateDTO.password()
        );
    }

    @Test
    @DisplayName("login should return LoginResponseDTO when credentials are valid")
    void testLogin_Success() {
        // Given: Repository returns a valid LoginProjection
        when(loginRepository.login(
            loginRequestDTO.username(),
            loginRequestDTO.password()
        )).thenReturn(Optional.of(loginProjection));

        // When: Service login is called
        LoginResponseDTO result = loginService.login(loginRequestDTO);

        // Then: Verify result and repository interaction
        assertNotNull(result);
        assertEquals(1, result.userId());
        assertEquals("Test User", result.name());
        assertEquals(LocalDateTime.of(2026, 4, 30, 10, 30), result.lastLogin());
        verify(loginRepository, times(1)).login(
            loginRequestDTO.username(),
            loginRequestDTO.password()
        );
    }

    @Test
    @DisplayName("login should throw ResourceNotFoundException when credentials are invalid")
    void testLogin_InvalidCredentials() {
        // Given: Repository returns empty Optional
        when(loginRepository.login(
            loginRequestDTO.username(),
            loginRequestDTO.password()
        )).thenReturn(Optional.empty());

        // When & Then: Service should throw ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> loginService.login(loginRequestDTO)
        );

        assertTrue(exception.getMessage().contains("Invalid username or password"));
        verify(loginRepository, times(1)).login(
            loginRequestDTO.username(),
            loginRequestDTO.password()
        );
    }

}
