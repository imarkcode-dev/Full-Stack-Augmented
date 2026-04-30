package com.nexus.app.user.service;


import com.nexus.app.user.domain.dto.LoginCreateDTO;
import com.nexus.app.user.domain.dto.LoginRequestDTO;
import com.nexus.app.user.domain.dto.LoginResponseDTO;

public interface ILoginService {

    Boolean createLogin(LoginCreateDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

}
