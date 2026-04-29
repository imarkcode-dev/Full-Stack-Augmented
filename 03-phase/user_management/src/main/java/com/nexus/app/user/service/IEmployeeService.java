package com.nexus.app.user.service;

import java.util.List;

import com.nexus.app.user.domain.dto.EmployeeRequestDTO;
import com.nexus.app.user.domain.dto.EmployeeResponseDTO;

public interface IEmployeeService {

    List<EmployeeResponseDTO> findAll();

    EmployeeResponseDTO findById(Integer id);

    EmployeeResponseDTO create(EmployeeRequestDTO employeeDto);

    EmployeeResponseDTO update(Integer id, EmployeeRequestDTO employeeDto);

    void delete(Integer id);



}
