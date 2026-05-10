package com.smart.billing.app.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart.billing.app.domain.Contract;
import com.smart.billing.app.domain.Invoice;
import com.smart.billing.app.dto.InvoiceRequestDTO;
import com.smart.billing.app.dto.InvoiceResponseDTO;
import com.smart.billing.app.exception.ResourceNotFoundException;
import com.smart.billing.app.repository.ContractRepository;
import com.smart.billing.app.repository.InvoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final ContractRepository contractRepository;

    @Transactional(readOnly = true)
    public List<InvoiceResponseDTO> findAll() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDTO findById(Integer id) {
        return invoiceRepository.findById(id)
            .map(this::mapToResponse)
            .orElseThrow( () -> new ResourceNotFoundException("Invoice not found with id: " + id) );
    }

    @Transactional
    public InvoiceResponseDTO create(InvoiceRequestDTO dto) {
        
        Contract contract = contractRepository.findById(dto.contractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with ID: " + dto.contractId()));

        
        if (invoiceRepository.existsByInvoiceNumber(dto.invoiceNumber())) {
            throw new RuntimeException("Invoice number " + dto.invoiceNumber() + " already exists.");
        }

        
        Invoice invoice = Invoice.builder()
                .contract(contract)
                .invoiceNumber(dto.invoiceNumber())
                .issueDate(dto.issueDate())
                .dueDate(dto.dueDate())
                .totalAmount(contract.getMonthlyFee()) 
                .penaltyAmount(dto.penaltyAmount() != null ? dto.penaltyAmount() : BigDecimal.ZERO)
                .status("PENDING")
                .build();

        return mapToResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public InvoiceResponseDTO update(Integer id, InvoiceRequestDTO dto) {
        
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));

        int customerId = contractRepository.findById(dto.contractId())
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"))
            .getCustomer()
            .getId();

        
        if (!invoice.getContract().getId().equals(dto.contractId())) {
            Contract newContract = contractRepository.findById(customerId) 
                    .orElseThrow(() -> new ResourceNotFoundException("New Contract not found with ID: " + dto.contractId()));
            
            invoice.setContract(newContract);
            invoice.setTotalAmount(newContract.getMonthlyFee()); 
        }

        if (!invoice.getInvoiceNumber().equals(dto.invoiceNumber())) {
            if (invoiceRepository.existsByInvoiceNumber(dto.invoiceNumber())) {
                throw new RuntimeException("Invoice number " + dto.invoiceNumber() + " is already in use by another record.");
            }
            invoice.setInvoiceNumber(dto.invoiceNumber());
        }

        invoice.setIssueDate(dto.issueDate());
        invoice.setDueDate(dto.dueDate());
        invoice.setPenaltyAmount(dto.penaltyAmount() != null ? dto.penaltyAmount() : BigDecimal.ZERO);

        Invoice invoiceUpdated = invoiceRepository.save(invoice);
        return mapToResponse(invoiceUpdated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found with ID: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    

    private InvoiceResponseDTO mapToResponse(Invoice entity) {
        return new InvoiceResponseDTO(
                entity.getId(),
                entity.getInvoiceNumber(),
                entity.getContract().getCustomer().getNameCustomer(),
                entity.getContract().getTitle(),
                entity.getIssueDate(),
                entity.getDueDate(),
                entity.getTotalAmount(),
                entity.getPenaltyAmount(),
                entity.getStatus()
        );
    }

}
