package com.nexus.app.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexus.app.user.domain.dto.EmployeeRequestDTO;
import com.nexus.app.user.domain.dto.EmployeeResponseDTO;
import com.nexus.app.user.domain.entity.EmployeeEntity;
import com.nexus.app.user.exception.ResourceNotFoundException;
import com.nexus.app.user.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeResponseDTO> findAll() {
        return employeeRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Override
    public EmployeeResponseDTO findById(Integer id) {
        return employeeRepository.findById(id)
            .map(this::mapToResponse)
            .orElseThrow( () -> new ResourceNotFoundException("Employee not found with id: " + id));
            
    }

    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO employeeDto) {
        EmployeeEntity entity = mapToEntity(employeeDto);
        EmployeeEntity saved = employeeRepository.save(entity);
        return mapToResponse(saved);
    }

    @Override
    public EmployeeResponseDTO update(Integer id, EmployeeRequestDTO employeeDto) {

        EmployeeEntity entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        entity.setName(employeeDto.name());
        entity.setLastName(employeeDto.lastName());
        entity.setEmail(employeeDto.email());
        entity.setPhone(employeeDto.phone());

        EmployeeEntity updated = employeeRepository.save(entity);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Integer id) {

        EmployeeEntity entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(entity);
    }

    private EmployeeResponseDTO mapToResponse(EmployeeEntity entity) {
        return new EmployeeResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

     private EmployeeEntity mapToEntity(EmployeeRequestDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setName(dto.name());
        entity.setLastName(dto.lastName());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        return entity;
    }

}
