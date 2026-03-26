// Create a Rest Controller for Employee with endpoints
// Java 17
// Spring Boot 3
// Endpoints:
// GET /api/empleados - Get all employees
// GET /api/empleados/{id} - Get employee by id
// POST /api/empleados - Create a new employee
// PUT /api/empleados/{id} - Update an existing employee
// DELETE /api/empleados/{id} - Delete an employee
// Use ResponseEntity all edpoints



package com.example.controller;

import com.example.entity.Empleado;
import com.example.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Employee with endpoints
 * Java 17
 * Spring Boot 3
 */
@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    /**
     * GetAll Employees
     * @return ResponseEntity with list of all employees
     */
    @GetMapping
    public ResponseEntity<List<Empleado>> getAllEmpleados() {
        List<Empleado> empleados = empleadoService.getAllEmpleados();
        return new ResponseEntity<>(empleados, HttpStatus.OK);
    }

    /**
     * GetById Employee
     * @param id the employee id
     * @return ResponseEntity with the employee if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmpleadoById(@PathVariable Long id) {
        Optional<Empleado> empleado = empleadoService.getEmpleadoById(id);
        if (empleado.isPresent()) {
            return new ResponseEntity<>(empleado.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Empleado not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create Employee
     * @param empleado the employee object to create
     * @return ResponseEntity with the created employee
     */
    @PostMapping
    public ResponseEntity<Empleado> createEmpleado(@RequestBody Empleado empleado) {
        Empleado createdEmpleado = empleadoService.saveEmpleado(empleado);
        return new ResponseEntity<>(createdEmpleado, HttpStatus.CREATED);
    }

    /**
     * Update Employee
     * @param id the employee id to update
     * @param empleadoDetails the updated employee details
     * @return ResponseEntity with the updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmpleado(@PathVariable Long id, @RequestBody Empleado empleadoDetails) {
        Optional<Empleado> empleado = empleadoService.getEmpleadoById(id);
        if (empleado.isPresent()) {
            Empleado existingEmpleado = empleado.get();
            if (empleadoDetails.getNombre() != null) {
                existingEmpleado.setNombre(empleadoDetails.getNombre());
            }
            if (empleadoDetails.getApellido() != null) {
                existingEmpleado.setApellido(empleadoDetails.getApellido());
            }
            if (empleadoDetails.getEmail() != null) {
                existingEmpleado.setEmail(empleadoDetails.getEmail());
            }
            Empleado updatedEmpleado = empleadoService.saveEmpleado(existingEmpleado);
            return new ResponseEntity<>(updatedEmpleado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Empleado not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete Employee
     * @param id the employee id to delete
     * @return ResponseEntity with the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable Long id) {
        Optional<Empleado> empleado = empleadoService.getEmpleadoById(id);
        if (empleado.isPresent()) {
            empleadoService.deleteEmpleado(id);
            return new ResponseEntity<>("Empleado deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Empleado not found", HttpStatus.NOT_FOUND);
        }
    }
}

