package com.smart.billing.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.billing.app.dto.CustomerRequestDTO;
import com.smart.billing.app.dto.CustomerResponseDTO;
import com.smart.billing.app.service.ICustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO response = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(
            @PathVariable Integer id, 
            @Valid @RequestBody CustomerRequestDTO request) {

        CustomerResponseDTO response = customerService.update(id, request);

         return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}   
