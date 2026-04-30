package com.nexus.app.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nexus.app.user.domain.dto.LoginCreateDTO;
import com.nexus.app.user.domain.dto.LoginRequestDTO;
import com.nexus.app.user.domain.dto.LoginResponseDTO;
import com.nexus.app.user.domain.projection.LoginProjection;
import com.nexus.app.user.exception.ResourceNotFoundException;
import com.nexus.app.user.repository.LoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {

    private final LoginRepository loginRepository;
    

    @Override
    public Boolean createLogin(LoginCreateDTO loginDTO) {
    
       return Optional.ofNullable(
            loginRepository.createLogin(
                    loginDTO.userId(),
                    loginDTO.username(),
                    loginDTO.password() )
        ).filter(Boolean::booleanValue)
        .orElseThrow(() -> new IllegalStateException("Login could not be created"));
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginDto) {

         LoginProjection result = loginRepository
            .login(loginDto.username(), loginDto.password())
            .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        return new LoginResponseDTO(result.getUserId(), result.getName(), result.getLastLogin());
    }


}
