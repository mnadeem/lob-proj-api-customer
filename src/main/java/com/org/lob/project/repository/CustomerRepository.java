package com.org.lob.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.lob.project.repository.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    //Contains search on either firstname or lastname
    List<Customer> findAllByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    Optional<Customer> findCustomerByEmailAddress(String email);
}
