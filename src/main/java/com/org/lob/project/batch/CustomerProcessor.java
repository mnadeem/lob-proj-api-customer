package com.org.lob.project.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemProcessor;

import com.org.lob.project.batch.model.AddressData;
import com.org.lob.project.batch.model.CustomerData;
import com.org.lob.project.repository.entity.Address;
import com.org.lob.project.repository.entity.Customer;

public class CustomerProcessor implements ItemProcessor<CustomerData, Customer>{

	@Override
	public Customer process(CustomerData item) throws Exception {
		Customer customer = new Customer();
		customer.setId(item.getId());
		customer.setEmailAddress(item.getEmailAddress());
		customer.setFirstName(item.getFirstName());
		customer.setLastName(item.getLastName());
		customer.setAddresses(buildAddress(item.getAddresses()));
		return customer;
	}

	private List<Address> buildAddress(List<AddressData> addresses) {
		return addresses.stream().map(ad -> buildAddrss(ad)).collect(Collectors.toList());
	}

	private Address buildAddrss(AddressData ad) {
		Address address = new Address();
		address.setCity(ad.getCity());
		address.setStateCode(ad.getStateCode());
		address.setCountry(ad.getCountry());
		address.setId(ad.getId());
		address.setStateCode(ad.getStateCode());
		address.setStreetAddress(ad.getStreetAddress());
		
		return address;
	}
}
