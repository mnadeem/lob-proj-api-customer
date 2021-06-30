package com.org.lob.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.org.lob.project.service.model.CustomerModel;
import com.org.lob.project.service.model.CustomerSearchRequest;

public interface CustomerService {

	Optional<CustomerModel> getCustomerById(Long customerId);

	List<CustomerModel> findAllById(Iterable<Long> ids);

	CustomerModel create(CustomerModel customerModel);

	CustomerModel update(CustomerModel customerModel);

	List<CustomerModel> findByName(String name);

	Optional<CustomerModel> findByEmail(String email);

	Page<CustomerModel> findAll(Pageable pageable);

	Page<CustomerModel> search(CustomerSearchRequest request, Pageable pageable);

	void deleteCustomer(Long customerId);
}
