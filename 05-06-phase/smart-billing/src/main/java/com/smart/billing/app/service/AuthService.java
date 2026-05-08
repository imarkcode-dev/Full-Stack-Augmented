package com.smart.billing.app.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart.billing.app.config.JwtService;
import com.smart.billing.app.domain.LoginUser;
import com.smart.billing.app.dto.LoginRequestDTO;
import com.smart.billing.app.dto.LoginResponseDTO;
import com.smart.billing.app.dto.RegisterRequestDTO;
import com.smart.billing.app.exception.ResourceNotFoundException;
import com.smart.billing.app.repository.AuthRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        // Locate user by email
        LoginUser user = authRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        // Verify password with BCrypt
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials.");
        }

        // Record access audit
        user.setLastLogin(LocalDateTime.now());
        authRepository.save(user);

        String lastLoginStr = user.getLastLogin().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Create DTO for Claims generation in JWT
        LoginResponseDTO tempDto = new LoginResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getLastName(),
                user.getAuthProvider(),
                user.getRoleUser(),
                lastLoginStr,
                null
        );

        String jwtToken = jwtService.generateToken(tempDto);

        // Return final response with the Token
        return new LoginResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getLastName(),
                user.getAuthProvider(),
                user.getRoleUser(),
                lastLoginStr,
                jwtToken
        );
    }

    @Override
    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO request) {
        // Check if the email address already exists to avoid duplicates
        if (authRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("The email address is already registered.");
        }

        // Create an instance of the entity and map the data
        LoginUser newUser = new LoginUser();
        newUser.setEmail(request.email());
        // ENCRYPTION
        newUser.setPasswordHash(passwordEncoder.encode(request.password()));
        newUser.setUserName(request.userName());
        newUser.setLastName(request.lastName());
        newUser.setAuthProvider(request.authProvider() != null ? request.authProvider() : "LOCAL");
        newUser.setRoleUser(request.roleUser() != null ? request.roleUser() : "ROLE_USER");
        newUser.setLastLogin(LocalDateTime.now());

        // Persist on login_user
        LoginUser savedUser = authRepository.save(newUser);

        // Generate response
        String lastLoginStr = savedUser.getLastLogin().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        LoginResponseDTO responseDTO = new LoginResponseDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUserName(),
                savedUser.getLastName(),
                savedUser.getAuthProvider(),
                savedUser.getRoleUser(),
                lastLoginStr,
                null 
        );

        // Generate JWT for the new user
        String token = jwtService.generateToken(responseDTO);

        return new LoginResponseDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUserName(),
                savedUser.getLastName(),
                savedUser.getAuthProvider(),
                savedUser.getRoleUser(),
                lastLoginStr,
                token
        );
    }



}
