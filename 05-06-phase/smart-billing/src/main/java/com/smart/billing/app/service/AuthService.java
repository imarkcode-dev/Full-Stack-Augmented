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
        LoginUser user = authRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid credentials.");
        }

        user.setLastLogin(LocalDateTime.now());
        authRepository.save(user);

        return generateLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO request) {
        if (authRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("The email address is already registered.");
        }

        LoginUser newUser = new LoginUser();
        newUser.setEmail(request.email());
        newUser.setPasswordHash(passwordEncoder.encode(request.password()));
        newUser.setUsername(request.userName());
        newUser.setLastName(request.lastName());
        newUser.setAuthProvider(request.authProvider() != null ? request.authProvider() : "LOCAL");
        newUser.setRoleUser(request.roleUser() != null ? request.roleUser() : "ROLE_USER");
        newUser.setLastLogin(LocalDateTime.now());

        LoginUser savedUser = authRepository.save(newUser);

        return generateLoginResponse(savedUser);
    }

    private LoginResponseDTO generateLoginResponse(LoginUser user) {
        String lastLoginStr = user.getLastLogin().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
        LoginResponseDTO response = new LoginResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getLastName(),
                user.getAuthProvider(),
                user.getRoleUser(),
                lastLoginStr,
                null 
        );

        String token = jwtService.generateToken(response);

        return new LoginResponseDTO(
                response.id(),
                response.email(),
                response.username(),
                response.lastName(),
                response.authProvider(),
                response.roleUser(),
                response.lastLogin(),
                token
        );
    }


}
