package com.org.lob.project.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.org.lob.project.repository.entity.Address;
import com.org.lob.project.service.model.AddressModel;

@Mapper(componentModel = "spring")
public interface AddressMapper {

	AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

	List<AddressModel> toAddressModels(List<Address> source);

	List<Address> toAddresses(List<AddressModel> source);
}
