package com.smart.billing.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.billing.app.domain.Payment;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {


}
