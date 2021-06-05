package com.org.lob.project.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.org.lob.project.batch.model.AddressData;
import com.org.lob.project.batch.model.CustomerData;
import com.org.lob.project.repository.entity.Address;
import com.org.lob.project.repository.entity.Customer;

public class CustomerProcessor implements ItemProcessor<CustomerData, Customer> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerProcessor.class);

	@Override
	public Customer process(CustomerData item) throws Exception {
		LOGGER.trace("Processing {} ", item);
		return newCustomer(item);
	}

	private Customer newCustomer(CustomerData item) {
		Customer customer = new Customer();
		customer.setId(item.getId());
		customer.setEmailAddress(item.getEmailAddress());
		customer.setFirstName(item.getFirstName());
		customer.setLastName(item.getLastName());
		customer.setAddresses(buildAddress(customer, item.getAddresses()));
		return customer;
	}

	private List<Address> buildAddress(Customer customer, List<AddressData> addresses) {
		return addresses.stream().map(ad -> buildAddrss(customer, ad)).collect(Collectors.toList());
	}

	private Address buildAddrss(Customer customer, AddressData ad) {
		Address address = new Address();
		address.setCity(ad.getCity());
		address.setStateCode(ad.getStateCode());
		address.setCountry(ad.getCountry());
		address.setId(ad.getId());
		address.setStateCode(ad.getStateCode());
		address.setStreetAddress(ad.getStreetAddress());
		address.setCustomer(customer);
		return address;
	}
}
