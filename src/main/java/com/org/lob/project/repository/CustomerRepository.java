package com.org.lob.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import com.org.lob.project.repository.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, RevisionRepository<Customer, Long, Long>, JpaSpecificationExecutor<Customer> {

	Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
    List<Customer> findAll(Specification<Customer> spec);

    //Contains search on either firstname or lastname
    List<Customer> findAllByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    Optional<Customer> findCustomerByEmailAddress(String email);
}
