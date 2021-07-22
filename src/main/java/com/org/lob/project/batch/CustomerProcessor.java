package com.org.lob.project.batch;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.org.lob.project.batch.model.AddressData;
import com.org.lob.project.batch.model.CustomerData;
import com.org.lob.project.service.model.AddressModel;
import com.org.lob.project.service.model.CustomerModel;

public class CustomerProcessor implements ItemProcessor<CustomerData, CustomerModel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerProcessor.class);

	@Override
	public CustomerModel process(CustomerData item) throws Exception {
		LOGGER.trace("Processing {} ", item);
		return newCustomer(item);
	}

	private CustomerModel newCustomer(CustomerData item) {
		CustomerModel customer = new CustomerModel();
		customer.setId(item.getId());
		customer.setEmailAddress(item.getEmailAddress());
		customer.setFirstName(item.getFirstName());
		customer.setLastName(item.getLastName());
		customer.setAddresses(buildAddress(customer, item.getAddresses()));
		return customer;
	}

	private List<AddressModel> buildAddress(CustomerModel customer, List<AddressData> addresses) {
		return addresses.stream().map(ad -> buildAddrss(customer, ad)).collect(Collectors.toList());
	}

	private AddressModel buildAddrss(CustomerModel customer, AddressData ad) {
		AddressModel address = new AddressModel();
		address.setCity(ad.getCity());
		address.setStateCode(ad.getStateCode());
		address.setCountry(ad.getCountry());
		address.setId(ad.getId());
		address.setStateCode(ad.getStateCode());
		address.setStreetAddress(ad.getStreetAddress());
		address.setCustomerModel(customer);
		return address;
	}
}
