package com.smart.billing.app.service;

import java.util.List;

import com.smart.billing.app.dto.ContractRequestDTO;
import com.smart.billing.app.dto.ContractResponseDTO;


public interface IContractService {

    List<ContractResponseDTO> findAll();

    ContractResponseDTO findById(Integer id);

    ContractResponseDTO create(ContractRequestDTO dto);

    ContractResponseDTO update(Integer id, ContractRequestDTO dto);

    void delete(Integer id);

    List<ContractResponseDTO> findByCustomerId(Integer customerId);

    List<ContractResponseDTO> findByStatus(String status);


}
