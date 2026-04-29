package com.nexus.app.user.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

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
import com.nexus.app.user.domain.dto.EmployeeRequestDTO;
import com.nexus.app.user.domain.dto.EmployeeResponseDTO;
import com.nexus.app.user.exception.GlobalExceptionHandler;
import com.nexus.app.user.exception.ResourceNotFoundException;
import com.nexus.app.user.service.IEmployeeService;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeController Unit Tests")
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper;

    private EmployeeResponseDTO employeeResponse;
    private EmployeeRequestDTO employeeRequest;

    @BeforeEach
    void setUp() {
    
        objectMapper = new ObjectMapper();

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();


        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        employeeRequest = new EmployeeRequestDTO(
                "Obi Wan",
                "Kenobi",
                "kenobi@masterjedi.com",
                "666-1234"
        );

        employeeResponse = new EmployeeResponseDTO(
                1,
                "Obi Wan",
                "Kenobi",
                "kenobi@masterjedi.com",
                "666-1234",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void shouldReturnAllEmployees() throws Exception {
        when(employeeService.findAll()).thenReturn(List.of(employeeResponse));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(employeeService).findAll();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void shouldReturnEmployeeById() throws Exception {
        when(employeeService.findById(1)).thenReturn(employeeResponse);

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(employeeService).findById(1);
    }

    @Test
    void shouldReturn404WhenEmployeeNotFound() throws Exception {
        when(employeeService.findById(999))
                .thenThrow(new ResourceNotFoundException("not found"));

        mockMvc.perform(get("/api/v1/employees/999"))
                .andExpect(status().isNotFound());

        verify(employeeService).findById(999);
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void shouldCreateEmployee() throws Exception {
        when(employeeService.create(any(EmployeeRequestDTO.class)))
                .thenReturn(employeeResponse);

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Obi Wan"))
                .andExpect(jsonPath("$.email").value("kenobi@masterjedi.com"))
                ;

        verify(employeeService).create(any(EmployeeRequestDTO.class));
    }

   @Test
    void shouldRejectInvalidRequest() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).create(any(EmployeeRequestDTO.class));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void shouldUpdateEmployee() throws Exception {
        when(employeeService.update(eq(1), any(EmployeeRequestDTO.class)))
                .thenReturn(employeeResponse);

        mockMvc.perform(put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isOk());

        verify(employeeService).update(eq(1), any(EmployeeRequestDTO.class));
    }

    @Test
    void shouldReturn404WhenUpdateNotFound() throws Exception {
        when(employeeService.update(eq(999), any(EmployeeRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("not found"));

        mockMvc.perform(put("/api/v1/employees/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isNotFound());

        verify(employeeService).update(eq(999), any(EmployeeRequestDTO.class));
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void shouldDeleteEmployee() throws Exception {
        doNothing().when(employeeService).delete(1);

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService).delete(1);
    }

    @Test
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("not found"))
                .when(employeeService).delete(999);

        mockMvc.perform(delete("/api/v1/employees/999"))
                .andExpect(status().isNotFound());

        verify(employeeService).delete(999);
    }



}