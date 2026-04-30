package com.nexus.app.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.app.user.domain.dto.LoginCreateDTO;
import com.nexus.app.user.domain.dto.LoginRequestDTO;
import com.nexus.app.user.domain.dto.LoginResponseDTO;
import com.nexus.app.user.service.ILoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<Void> createLogin(
        @Valid @RequestBody LoginCreateDTO request) {

        loginService.createLogin(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
        @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = loginService.login(request);

        return ResponseEntity.ok(response);
    }

}
