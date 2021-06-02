package com.org.lob.project.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.org.lob.project.repository.CustomerRepository;
import com.org.lob.project.repository.entity.Customer;

@Service
public class CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	private CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Optional<Customer> getCustomerById(Long customerId) {
		LOGGER.info("Fetching customer by id: {}", customerId);
		return customerRepository.findById(customerId);
	}

	public Customer create(Customer customer) {
		try {
			LOGGER.info("Creating a new customer with emailAddress: {}", customer.getEmailAddress());
			return customerRepository.save(customer);
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Customer already exists with emailAddress: {}", customer.getEmailAddress());
			throw new RuntimeException("Customer already exists with same emailAddress");
		}
	}

	public Customer update(Customer customer) {
		LOGGER.info("Updating a customer with id: {}", customer.getId());
		Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
		if (optionalCustomer.isEmpty()) {
			LOGGER.error("Unable to update customer by id {}", customer.getId());
			throw new RuntimeException("Customer does not exists");
		}
		Customer existingCustomer = optionalCustomer.get();
		existingCustomer.setAddresses(customer.getAddresses());
		existingCustomer.setFirstName(customer.getFirstName());
		existingCustomer.setLastName(customer.getLastName());
		return customerRepository.save(existingCustomer);
	}

	public List<Customer> findByName(String name) {
		return customerRepository.findAllByFirstNameContainingOrLastNameContaining(name, name);
	}

	public Optional<Customer> findByEmail(String email) {
		return customerRepository.findCustomerByEmailAddress(email);
	}

	// Paging implementation of findAll
	public Page<Customer> findAll(Pageable pageable) {
		return customerRepository.findAll(pageable);
	}

	public void deleteCustomer(Long customerId) {
		try {
			customerRepository.deleteById(customerId);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Unable to delete customer by id {}", customerId);
			throw new RuntimeException("Customer does not exists");
		}
	}
}
