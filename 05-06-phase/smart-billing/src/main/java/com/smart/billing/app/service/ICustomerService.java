package com.smart.billing.app.service;

import java.util.List;

import com.smart.billing.app.dto.CustomerResponseDTO;
import com.smart.billing.app.dto.CustomerRequestDTO;

public interface ICustomerService {

    List<CustomerResponseDTO> findAll();
    
    CustomerResponseDTO findById(Integer id);

    CustomerResponseDTO create(CustomerRequestDTO dto);

     CustomerResponseDTO update(Integer id, CustomerRequestDTO dto);

     void delete(Integer id);


}
