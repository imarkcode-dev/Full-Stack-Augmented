package com.smart.billing.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.billing.app.domain.Customer;
import com.smart.billing.app.dto.CustomerRequestDTO;
import com.smart.billing.app.dto.CustomerResponseDTO;
import com.smart.billing.app.exception.ResourceNotFoundException;
import com.smart.billing.app.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO findById(Integer id) {
        return customerRepository.findById(id)
            .map(this::mapToResponse)
            .orElseThrow( () -> new ResourceNotFoundException("Customer not found with id: " + id) );
    }

    @Override
    @Transactional
    public CustomerResponseDTO create(CustomerRequestDTO dto) {

       if(dto == null) {
         throw new ResourceNotFoundException("CustomerRequestDTO cannot be null");
       }

       if (customerRepository.existsByTaxId(dto.taxId())) {
        throw new ResourceNotFoundException("The Tax ID " + dto.taxId() + " is already registered.");
       }
       
        Customer customerEntity = Customer.builder()
                .taxId(dto.taxId())
                .nameCustomer(dto.nameCustomer())
                .email(dto.email())
                .phone(dto.phone())
                .riskScore(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();

       Customer customerSaved = customerRepository.save(customerEntity);

       return mapToResponse(customerSaved);


    }

    @Override
    @Transactional
    public CustomerResponseDTO update(Integer id, CustomerRequestDTO dto) {

        Customer customerEntity = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        if (!customerEntity.getTaxId().equals(dto.taxId())) {
            if (customerRepository.existsByTaxId(dto.taxId())) {
                throw new ResourceNotFoundException("The Tax ID " + dto.taxId() + " is already used by another customer.");
            }
            customerEntity.setTaxId(dto.taxId());
        }

        customerEntity.setNameCustomer(dto.nameCustomer());
        customerEntity.setEmail(dto.email());
        customerEntity.setPhone(dto.phone());

        Customer customerUpdated = customerRepository.save(customerEntity);
        
        return mapToResponse(customerUpdated);
    }

   
    @Override
    @Transactional
    public void delete(Integer id) {
        
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerResponseDTO mapToResponse(Customer entity) {
        return new CustomerResponseDTO(
            entity.getId(),
            entity.getTaxId(),
            entity.getNameCustomer(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getRiskScore(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private Customer mapToEntity(CustomerResponseDTO dto) {
            Customer entity  = new Customer();

            entity.setId(dto.id());
            entity.setTaxId(dto.taxId());
            entity.setNameCustomer(dto.nameCustomer());
            entity.setEmail(dto.email());
            entity.setPhone(dto.phone());
            entity.setRiskScore(dto.riskScore());
            entity.setStatus(dto.status());
            entity.setCreatedAt(dto.createdAt());
            entity.setUpdatedAt(dto.updatedAt());
            return entity;
    }

    

}
