/*
Generate unit tests for EmployeeService using JUnit 5 and Mockito.

Requirements:
- Use @ExtendWith(MockitoExtension.class)
- Mock EmployeeRepository
- Test all methods:
    - findAll (list and empty)
    - findById (success and not found)
    - create
    - update (success and not found)
    - delete (success and not found)
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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexus.app.user.domain.dto.EmployeeRequestDTO;
import com.nexus.app.user.domain.dto.EmployeeResponseDTO;
import com.nexus.app.user.domain.entity.EmployeeEntity;
import com.nexus.app.user.exception.ResourceNotFoundException;
import com.nexus.app.user.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Unit Tests")
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeEntity employeeEntity;
    private EmployeeRequestDTO employeeRequestDTO;
    private EmployeeResponseDTO employeeResponseDTO;

    @BeforeEach
    void setUp() {
        // Given: Setup test data
        employeeEntity = EmployeeEntity.builder()
            .id(1)
            .name("Obi Wan")
            .lastName("Kenobi")
            .email("kenobi@masterjedi.com")
            .phone("666-1234")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        employeeRequestDTO = new EmployeeRequestDTO(
            "Obi Wan",
            "Kenobi",
            "kenobi@masterjedi.com",
            "666-1234"
        );

        employeeResponseDTO = new EmployeeResponseDTO(
            1,
            "Obi Wan",
            "Kenobi",
            "kenobi@masterjedi.com",
            "666-1234",
            employeeEntity.getCreatedAt(),
            employeeEntity.getUpdatedAt()
        );
    }

    @Test
    @DisplayName("findAll should return list of employees")
    void testFindAll_ReturnsListOfEmployees() {
        // Given: Repository returns a list of employees
        EmployeeEntity employee2 = EmployeeEntity.builder()
            .id(2)
            .name("Leia")
            .lastName("Skywalker")
            .email("lei.skywalker@java.com")
            .phone("666-4422")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(employeeRepository.findAll()).thenReturn(List.of(employeeEntity, employee2));

        // When: Service findAll is called
        List<EmployeeResponseDTO> result = employeeService.findAll();

        // Then: Verify results and interactions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Obi Wan", result.get(0).name());
        assertEquals("Leia", result.get(1).name());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll should return empty list when no employees exist")
    void testFindAll_ReturnsEmptyList() {
        // Given: Repository returns an empty list
        when(employeeRepository.findAll()).thenReturn(List.of());

        // When: Service findAll is called
        List<EmployeeResponseDTO> result = employeeService.findAll();

        // Then: Verify empty result
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById should return employee when found")
    void testFindById_Success() {
        // Given: Repository returns an employee
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));

        // When: Service findById is called
        EmployeeResponseDTO result = employeeService.findById(1);

        // Then: Verify result and interactions
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Obi Wan", result.name());
        assertEquals("kenobi@masterjedi.com", result.email());
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("findById should throw ResourceNotFoundException when employee not found")
    void testFindById_NotFound() {
        // Given: Repository returns empty optional
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: Service should throw exception
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> employeeService.findById(999)
        );

        assertTrue(exception.getMessage().contains("Employee not found"));
        verify(employeeRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("create should save and return new employee")
    void testCreate_Success() {
        // Given: Repository saves the employee
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        // When: Service create is called
        EmployeeResponseDTO result = employeeService.create(employeeRequestDTO);

        // Then: Verify result and interactions
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Obi Wan", result.name());
        assertEquals("Kenobi", result.lastName());
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("update should update existing employee")
    void testUpdate_Success() {
        // Given: Repository finds existing employee and saves updated version
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        EmployeeRequestDTO updateDTO = new EmployeeRequestDTO(
            "Leia",
            "Skywalker",
            "leia.skywalker@java.com",
            "666-4422"
        );

        // When: Service update is called
        EmployeeResponseDTO result = employeeService.update(1, updateDTO);

        // Then: Verify update was performed
        assertNotNull(result);
        assertEquals(1, result.id());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("update should throw ResourceNotFoundException when employee not found")
    void testUpdate_NotFound() {
        // Given: Repository returns empty optional
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: Service should throw exception
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> employeeService.update(999, employeeRequestDTO)
        );

        assertTrue(exception.getMessage().contains("Employee not found"));
        verify(employeeRepository, times(1)).findById(999);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("delete should remove existing employee")
    void testDelete_Success() {
        // Given: Repository finds existing employee
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));

        // When: Service delete is called
        employeeService.delete(1);

        // Then: Verify delete was performed
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).delete(employeeEntity);
    }

    @Test
    @DisplayName("delete should throw ResourceNotFoundException when employee not found")
    void testDelete_NotFound() {
        // Given: Repository returns empty optional
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: Service should throw exception
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> employeeService.delete(999)
        );

        assertTrue(exception.getMessage().contains("Employee not found"));
        verify(employeeRepository, times(1)).findById(999);
        verify(employeeRepository, never()).delete(any());
    }

}
