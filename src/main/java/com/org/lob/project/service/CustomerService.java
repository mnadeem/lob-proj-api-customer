package com.org.lob.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.model.CustomerSearchRequest;

public interface CustomerService {
	
	Optional<Customer> getCustomerById(Long customerId);

	Iterable<Customer> findAllById(Iterable<Long> ids);

	Customer create(Customer customer);
	
	Customer update(Customer customer);
	
	List<Customer> findByName(String name);
	
	Optional<Customer> findByEmail(String email);
	
	Page<Customer> findAll(Pageable pageable);

	Page<Customer> search(CustomerSearchRequest request, Pageable pageable);
	
	void deleteCustomer(Long customerId);
}
