package com.org.lob.project.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.org.lob.project.repository.CustomerRepository;
import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.mapper.AddressMapper;
import com.org.lob.project.service.mapper.CustomerMapper;
import com.org.lob.project.service.model.CustomerModel;

@ExtendWith({MockitoExtension.class})
class DefaultCustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private CustomerMapper customerMapper;
	@Mock
	private AddressMapper addressMapper;

	@InjectMocks
	private DefaultCustomerService targetBeingTested;

	@BeforeEach
	public void doBeforeEachTestCase() {
		//targetBeingTested = new DefaultCustomerService(customerRepository, customerMapper, addressMapper);
	}

	@Test
    void getCustomerByIdOptionalPresent() {
		Long id = 1L;
		when(customerRepository.findById(id)).thenReturn(Optional.of(customerWithIdOne(id)));
		when(customerMapper.toCustomerModel(any())).thenReturn(customerModelWithIdOne(id));
		
		Optional<CustomerModel> customerModel = targetBeingTested.getCustomerById(id);

		assertThat(customerModel).isPresent();       
    }

	private Customer customerWithIdOne(Long id) {
		Customer customerModel = new Customer();
		customerModel.setId(id);
		customerModel.setFirstName("john");
		return customerModel;
	}
	
	private CustomerModel customerModelWithIdOne(Long id) {
		CustomerModel customerModel = new CustomerModel();
		customerModel.setId(id);
		customerModel.setFirstName("john");
		return customerModel;
	}
}
