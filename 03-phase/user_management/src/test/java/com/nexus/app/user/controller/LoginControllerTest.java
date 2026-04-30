/*
Generate unit tests for LoginController using JUnit 5 and Mockito.

Requirements:
- Use @ExtendWith(MockitoExtension.class)
- Mock ILoginService
- Use MockMvc (standalone setup)
- Test endpoints:
    - POST /register (success, validation errors, internal error)
    - POST /login (success, validation errors, resource not found, internal error)
- Follow Given-When-Then structure
- Verify service interactions (verify, verifyNoInteractions, verifyNoMoreInteractions)
- Validate HTTP status codes and response body
- Cover edge cases: null, blank, malformed JSON, empty body
- Achieve 100% line and branch coverage
- Do not use @SpringBootTest
*/

package com.nexus.app.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.app.user.domain.dto.LoginCreateDTO;
import com.nexus.app.user.domain.dto.LoginRequestDTO;
import com.nexus.app.user.domain.dto.LoginResponseDTO;
import com.nexus.app.user.exception.GlobalExceptionHandler;
import com.nexus.app.user.exception.ResourceNotFoundException;
import com.nexus.app.user.service.ILoginService;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginController Unit Tests")
class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ILoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private ObjectMapper objectMapper;

    private LoginCreateDTO loginCreateRequest;
    private LoginRequestDTO loginRequest;
    private LoginResponseDTO loginResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        loginCreateRequest = new LoginCreateDTO(
                1,
                "testuser",
                "securePassword123"
        );

        loginRequest = new LoginRequestDTO(
                "testuser",
                "securePassword123"
        );

        loginResponse = new LoginResponseDTO(
                1,
                "Test User",
                LocalDateTime.of(2026, 4, 30, 10, 30)
        );
    }

    // =========================
    // REGISTER ENDPOINT TESTS
    // =========================

    @Test
    @DisplayName("POST /api/v1/auth/register should return 201 Created when login is registered successfully")
    void shouldRegisterLoginSuccessfully() throws Exception {
 
        when(loginService.createLogin(any(LoginCreateDTO.class)))
        .thenReturn(true);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCreateRequest)))
                .andExpect(status().isCreated());

        verify(loginService).createLogin(any(LoginCreateDTO.class));
    }


    @Test
    @DisplayName("POST /api/v1/auth/register should return 400 Bad Request when userId is null")
    void shouldRejectRegisterWithNullUserId() throws Exception {
        // Given: Request with null userId
        String invalidJson = "{\"username\": \"testuser\", \"password\": \"securePassword123\"}";

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).createLogin(any(LoginCreateDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register should return 400 Bad Request when username is blank")
    void shouldRejectRegisterWithBlankUsername() throws Exception {
        // Given: Request with blank username
        LoginCreateDTO invalidRequest = new LoginCreateDTO(1, "", "securePassword123");

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).createLogin(any(LoginCreateDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register should return 400 Bad Request when password is blank")
    void shouldRejectRegisterWithBlankPassword() throws Exception {
        // Given: Request with blank password
        LoginCreateDTO invalidRequest = new LoginCreateDTO(1, "testuser", "");

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).createLogin(any(LoginCreateDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register should return 400 Bad Request when request body is invalid")
    void shouldRejectRegisterWithInvalidJson() throws Exception {
        // Given: Invalid/empty request
        String invalidJson = "{}";

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).createLogin(any(LoginCreateDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register should return 400 Bad Request when JSON is malformed")
    void shouldRejectRegisterWithMalformedJson() throws Exception {
        // Given: Malformed JSON
        String malformedJson = "{invalid json}";

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).createLogin(any(LoginCreateDTO.class));
    }

    // =========================
    // LOGIN ENDPOINT TESTS
    // =========================
    @Test
    @DisplayName("POST /api/v1/auth/login should return 200 OK with LoginResponseDTO when credentials are valid")
    void shouldLoginSuccessfully() throws Exception {
        // Given: Service returns valid LoginResponseDTO
        when(loginService.login(any(LoginRequestDTO.class)))
                .thenReturn(loginResponse);

        // When & Then: Perform POST request and verify 200 response with body
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.lastLogin").isNotEmpty());

        verify(loginService, times(1)).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 404 Not Found when credentials are invalid")
    void shouldReturnNotFoundWhenCredentialsInvalid() throws Exception {
        // Given: Service throws ResourceNotFoundException
        when(loginService.login(any(LoginRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("Invalid username or password"));

        // When & Then: Perform POST request and expect 404
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound());

        verify(loginService, times(1)).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 400 Bad Request when username is blank")
    void shouldRejectLoginWithBlankUsername() throws Exception {
        // Given: Request with blank username
        LoginRequestDTO invalidRequest = new LoginRequestDTO("", "securePassword123");

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 400 Bad Request when password is blank")
    void shouldRejectLoginWithBlankPassword() throws Exception {
        // Given: Request with blank password
        LoginRequestDTO invalidRequest = new LoginRequestDTO("testuser", "");

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 400 Bad Request when request body is invalid")
    void shouldRejectLoginWithInvalidJson() throws Exception {
        // Given: Empty request
        String invalidJson = "{}";

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 400 Bad Request when JSON is malformed")
    void shouldRejectLoginWithMalformedJson() throws Exception {
        // Given: Malformed JSON
        String malformedJson = "{invalid json}";

        // When & Then: Perform POST request and expect 400
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());

        verify(loginService, never()).login(any(LoginRequestDTO.class));
    }

}
