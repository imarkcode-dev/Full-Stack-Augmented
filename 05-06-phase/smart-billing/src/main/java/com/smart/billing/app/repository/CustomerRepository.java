package com.smart.billing.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.smart.billing.app.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    Optional<Customer> findByTaxId(String taxId);
    
    boolean existsByTaxId(String taxId);
}
