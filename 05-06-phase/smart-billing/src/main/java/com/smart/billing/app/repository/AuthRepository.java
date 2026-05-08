package com.smart.billing.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.billing.app.domain.LoginUser;

public interface AuthRepository extends JpaRepository<LoginUser, Integer> {

   Optional<LoginUser> findByEmail(String email);
}
