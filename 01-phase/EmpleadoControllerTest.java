package com.example.controller;

import com.example.entity.Empleado;
import com.example.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit Tests for EmpleadoController
 * Uses JUnit 5, Mockito, and MockMvc
 * Tests all CRUD operations with success and failure scenarios
 */
@WebMvcTest(EmpleadoController.class)
@DisplayName("EmpleadoController Tests")
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpleadoService empleadoService;

    private Empleado empleado;
    private static final String API_URL = "/api/empleados";

    @BeforeEach
    void setUp() {
        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleado.setEmail("juan.perez@example.com");
    }

    // ==================== GET All Employees Tests ====================

    @Test
    @DisplayName("Should return all employees with status 200")
    void testGetAllEmpleados_Success() throws Exception {
        // Arrange
        Empleado empleado2 = new Empleado();
        empleado2.setId(2L);
        empleado2.setNombre("María");
        empleado2.setApellido("García");
        empleado2.setEmail("maria.garcia@example.com");

        List<Empleado> empleados = Arrays.asList(empleado, empleado2);
        when(empleadoService.getAllEmpleados()).thenReturn(empleados);

        // Act
        ResultActions result = mockMvc.perform(get(API_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[0].apellido", is("Pérez")))
                .andExpect(jsonPath("$[0].email", is("juan.perez@example.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("María")));

        verify(empleadoService, times(1)).getAllEmpleados();
    }

    @Test
    @DisplayName("Should return empty list when no employees exist")
    void testGetAllEmpleados_EmptyList() throws Exception {
        // Arrange
        when(empleadoService.getAllEmpleados()).thenReturn(Arrays.asList());

        // Act
        ResultActions result = mockMvc.perform(get(API_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(empleadoService, times(1)).getAllEmpleados();
    }

    // ==================== GET Employee by ID Tests ====================

    @Test
    @DisplayName("Should return employee by ID with status 200")
    void testGetEmpleadoById_Success() throws Exception {
        // Arrange
        Long empleadoId = 1L;
        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.of(empleado));

        // Act
        ResultActions result = mockMvc.perform(get(API_URL + "/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Pérez")))
                .andExpect(jsonPath("$.email", is("juan.perez@example.com")));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
    }

    @Test
    @DisplayName("Should return 404 when employee not found")
    void testGetEmpleadoById_NotFound() throws Exception {
        // Arrange
        Long empleadoId = 99L;
        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(get(API_URL + "/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Empleado not found")));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
    }

    // ==================== POST Create Employee Tests ====================

    @Test
    @DisplayName("Should create a new employee and return status 201")
    void testCreateEmpleado_Success() throws Exception {
        // Arrange
        Empleado newEmpleado = new Empleado();
        newEmpleado.setNombre("Carlos");
        newEmpleado.setApellido("López");
        newEmpleado.setEmail("carlos.lopez@example.com");

        Empleado savedEmpleado = new Empleado();
        savedEmpleado.setId(3L);
        savedEmpleado.setNombre("Carlos");
        savedEmpleado.setApellido("López");
        savedEmpleado.setEmail("carlos.lopez@example.com");

        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(savedEmpleado);

        // Act
        ResultActions result = mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmpleado)));

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.apellido", is("López")))
                .andExpect(jsonPath("$.email", is("carlos.lopez@example.com")));

        verify(empleadoService, times(1)).saveEmpleado(any(Empleado.class));
    }

    @Test
    @DisplayName("Should create employee with minimal data")
    void testCreateEmpleado_MinimalData() throws Exception {
        // Arrange
        Empleado newEmpleado = new Empleado();
        newEmpleado.setNombre("Ana");

        Empleado savedEmpleado = new Empleado();
        savedEmpleado.setId(4L);
        savedEmpleado.setNombre("Ana");

        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(savedEmpleado);

        // Act
        ResultActions result = mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmpleado)));

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.nombre", is("Ana")));

        verify(empleadoService, times(1)).saveEmpleado(any(Empleado.class));
    }

    // ==================== PUT Update Employee Tests ====================

    @Test
    @DisplayName("Should update an existing employee and return status 200")
    void testUpdateEmpleado_Success() throws Exception {
        // Arrange
        Long empleadoId = 1L;
        Empleado updateDetails = new Empleado();
        updateDetails.setNombre("Juan Carlos");
        updateDetails.setApellido("Pérez García");
        updateDetails.setEmail("juancarlos.perez@example.com");

        Empleado updatedEmpleado = new Empleado();
        updatedEmpleado.setId(empleadoId);
        updatedEmpleado.setNombre("Juan Carlos");
        updatedEmpleado.setApellido("Pérez García");
        updatedEmpleado.setEmail("juancarlos.perez@example.com");

        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.of(empleado));
        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(updatedEmpleado);

        // Act
        ResultActions result = mockMvc.perform(put(API_URL + "/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDetails)));

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Carlos")))
                .andExpect(jsonPath("$.apellido", is("Pérez García")))
                .andExpect(jsonPath("$.email", is("juancarlos.perez@example.com")));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
        verify(empleadoService, times(1)).saveEmpleado(any(Empleado.class));
    }

    @Test
    @DisplayName("Should update only specific fields of an employee")
    void testUpdateEmpleado_PartialUpdate() throws Exception {
        // Arrange
        Long empleadoId = 1L;
        Empleado updateDetails = new Empleado();
        updateDetails.setNombre("Juan Lucas");
        // apellido and email are null, should not be updated

        Empleado updatedEmpleado = new Empleado();
        updatedEmpleado.setId(empleadoId);
        updatedEmpleado.setNombre("Juan Lucas");
        updatedEmpleado.setApellido("Pérez");
        updatedEmpleado.setEmail("juan.perez@example.com");

        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.of(empleado));
        when(empleadoService.saveEmpleado(any(Empleado.class))).thenReturn(updatedEmpleado);

        // Act
        ResultActions result = mockMvc.perform(put(API_URL + "/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDetails)));

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Lucas")));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
        verify(empleadoService, times(1)).saveEmpleado(any(Empleado.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent employee")
    void testUpdateEmpleado_NotFound() throws Exception {
        // Arrange
        Long empleadoId = 99L;
        Empleado updateDetails = new Empleado();
        updateDetails.setNombre("Ghost");

        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(put(API_URL + "/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDetails)));

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Empleado not found")));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
        verify(empleadoService, never()).saveEmpleado(any(Empleado.class));
    }

    // ==================== DELETE Employee Tests ====================

    @Test
    @DisplayName("Should delete an existing employee and return status 204")
    void testDeleteEmpleado_Success() throws Exception {
        // Arrange
        Long empleadoId = 1L;
        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.of(empleado));
        doNothing().when(empleadoService).deleteEmpleado(empleadoId);

        // Act
        ResultActions result = mockMvc.perform(delete(API_URL + "/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$", is("Empleado deleted successfully")));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
        verify(empleadoService, times(1)).deleteEmpleado(empleadoId);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent employee")
    void testDeleteEmpleado_NotFound() throws Exception {
        // Arrange
        Long empleadoId = 99L;
        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.empty());

        // Act
        ResultActions result = mockMvc.perform(delete(API_URL + "/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Empleado not found")));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
        verify(empleadoService, never()).deleteEmpleado(any());
    }

    // ==================== CORS Tests ====================

    @Test
    @DisplayName("Should allow CORS requests")
    void testCorsHeaders() throws Exception {
        // Arrange
        when(empleadoService.getAllEmpleados()).thenReturn(Arrays.asList(empleado));

        // Act & Assert
        mockMvc.perform(get(API_URL)
                .header("Origin", "http://localhost:3000")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
