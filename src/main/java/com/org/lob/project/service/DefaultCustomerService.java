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
import com.org.lob.project.service.model.CustomerSearchRequest;
import com.org.lob.project.service.support.CustomerSpecification;
import com.org.lob.project.service.support.ProjectException;

@Service
public class DefaultCustomerService implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCustomerService.class);

	private final CustomerRepository customerRepository;

	public DefaultCustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public Optional<Customer> getCustomerById(Long customerId) {
		LOGGER.debug("Fetching customer by id: {}", customerId);
		return customerRepository.findById(customerId);
	}

	@Override
	public Customer create(Customer customer) {
		try {
			LOGGER.debug("Creating a new customer with emailAddress: {}", customer.getEmailAddress());
			return customerRepository.save(customer);
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Customer already exists with emailAddress: {}", customer.getEmailAddress());
			throw ProjectException.duplicateRecord("Customer already exists with same emailAddress", new String[] {customer.getEmailAddress()});
		}
	}

	@Override
	public Customer update(Customer customer) {
		LOGGER.debug("Updating a customer with id: {}", customer.getId());
		Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
		if (!optionalCustomer.isPresent()) {
			LOGGER.error("Unable to update customer by id {}", customer.getId());
			throw ProjectException.noRecordFound("Customer does not exists", new String[] {String.valueOf(customer.getId())});
		}
		Customer existingCustomer = optionalCustomer.get();
		existingCustomer.setAddresses(customer.getAddresses());
		existingCustomer.setFirstName(customer.getFirstName());
		existingCustomer.setLastName(customer.getLastName());
		return customerRepository.save(existingCustomer);
	}
	
	@Override
	public List<Customer> findByName(String name) {
		return customerRepository.findAllByFirstNameContainingOrLastNameContaining(name, name);
	}

	@Override
	public Optional<Customer> findByEmail(String email) {
		return customerRepository.findCustomerByEmailAddress(email);
	}

	// Paging implementation of findAll
	@Override
	public Page<Customer> findAll(Pageable pageable) {
		return customerRepository.findAll(pageable);
	}

	@Override
	public void deleteCustomer(Long customerId) {
		try {
			customerRepository.deleteById(customerId);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Unable to delete customer by id {}", customerId);
			throw ProjectException.noRecordFound("Customer does not exists", e, new String[] {String.valueOf(customerId)});
		}
	}

	@Override
	public Page<Customer> search(CustomerSearchRequest request, Pageable pageable) {
		return customerRepository.findAll(new CustomerSpecification(request), pageable);
	}

	@Override
	public Iterable<Customer> findAllById(Iterable<Long> ids) {
		return customerRepository.findAllById(ids);
	}
}
