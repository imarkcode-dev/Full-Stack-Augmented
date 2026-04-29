package com.nexus.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.app.user.domain.entity.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository <EmployeeEntity, Integer> {

}
