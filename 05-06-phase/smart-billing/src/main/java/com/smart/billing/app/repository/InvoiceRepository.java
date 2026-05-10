package com.smart.billing.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.billing.app.domain.Invoice;

@Repository
public interface InvoiceRepository  extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceNumber(String invoiceNumber);

}
