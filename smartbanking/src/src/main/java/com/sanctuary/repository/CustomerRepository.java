package com.sanctuary.repository;

import com.sanctuary.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    List<Customer> findByFullNameContainingIgnoreCase(String name);
}
