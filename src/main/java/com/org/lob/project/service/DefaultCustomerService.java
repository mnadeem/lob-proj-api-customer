package com.org.lob.project.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.org.lob.project.exception.ProjectException;
import com.org.lob.project.repository.CustomerRepository;
import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.mapper.AddressMapper;
import com.org.lob.project.service.mapper.CustomerMapper;
import com.org.lob.project.service.model.CustomerModel;
import com.org.lob.project.service.model.CustomerSearchRequest;
import com.org.lob.project.service.specification.CustomerSpecification;

@Service
public class DefaultCustomerService implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCustomerService.class);

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	private final AddressMapper addressMapper;

	public DefaultCustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, AddressMapper addressMapper) {
		this.customerRepository = customerRepository;
		this.customerMapper = customerMapper;
		this.addressMapper = addressMapper;
	}

	@Override
	public Optional<CustomerModel> getCustomerById(Long customerId) {
		LOGGER.debug("Fetching customer by id: {}", customerId);
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		return optionalCustomer.isEmpty() ? Optional.empty(): Optional.of(customerMapper.toCustomerModel(optionalCustomer.get()));
	}

	@Override
	public CustomerModel create(CustomerModel customerdata) {
		try {
			LOGGER.debug("Creating a new customer with emailAddress: {}", customerdata.getEmailAddress());
			return customerMapper.toCustomerModel(customerRepository.save(customerMapper.toCustomer(customerdata)));
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("Customer already exists with emailAddress: {}", customerdata.getEmailAddress());
			throw ProjectException.duplicateRecord("Customer already exists with same emailAddress " + customerdata.getEmailAddress());
		}
	}

	@Override
	public CustomerModel update(CustomerModel customerData) {
		LOGGER.debug("Updating a customer with id: {}", customerData.getId());
		Optional<Customer> optionalCustomer = customerRepository.findById(customerData.getId());
		if (!optionalCustomer.isPresent()) {
			LOGGER.error("Unable to update customer by id {}", customerData.getId());
			throw ProjectException.noRecordFound("Customer does not exists " + customerData.getId());
		}
		Customer existingCustomer = optionalCustomer.get();
		existingCustomer.setAddresses(addressMapper.toAddressList(customerData.getAddresses()));
		existingCustomer.setFirstName(customerData.getFirstName());
		existingCustomer.setLastName(customerData.getLastName());
		return customerMapper.toCustomerModel(customerRepository.save(existingCustomer));
	}
	
	@Override
	public List<CustomerModel> findByName(String name) {
		return customerMapper.toCustomerModelList(customerRepository.findAllByFirstNameContainingOrLastNameContaining(name, name));
	}

	@Override
	public Optional<CustomerModel> findByEmail(String email) {
		Optional<Customer> optionalCustomer = customerRepository.findCustomerByEmailAddress(email);
		return optionalCustomer.isEmpty() ? Optional.empty(): Optional.of(customerMapper.toCustomerModel(optionalCustomer.get()));
	}

	// Paging implementation of findAll
	@Override
	public Page<CustomerModel> findAll(Pageable pageable) {
		return new PageImpl<CustomerModel>( customerMapper.toCustomerModelList(customerRepository.findAll(pageable).getContent()));
	}

	@Override
	public void deleteCustomer(Long customerId) {
		try {
			customerRepository.deleteById(customerId);
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("Unable to delete customer by id {}", customerId);
			throw ProjectException.noRecordFound("Customer does not exists " + customerId);
		}
	}

	@Override
	public Page<CustomerModel> search(CustomerSearchRequest request, Pageable pageable) {
		return new PageImpl<CustomerModel>(customerMapper.toCustomerModelList(customerRepository.findAll(new CustomerSpecification(request), pageable).getContent()));
	}

	@Override
	public List<CustomerModel> findAllById(Iterable<Long> ids) {
		return customerMapper.toCustomerModelList(customerRepository.findAllById(ids));
	}
}
