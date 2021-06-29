package com.org.lob.project.service.support;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.model.CustomerModel;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface CustomerMapper {

	CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

	Customer toCustomer(CustomerModel source);

	CustomerModel toCustomerModel(Customer source);

	List<CustomerModel> toCustomerModelList(List<Customer> source);
}
