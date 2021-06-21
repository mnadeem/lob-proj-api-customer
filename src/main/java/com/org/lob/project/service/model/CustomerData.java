package com.org.lob.project.service.model;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class CustomerData {

    private Long id;
    private String firstName;
    private String lastName;

	@NotBlank(message = "{email.not_empty}")
    private String emailAddress;

    @JsonManagedReference
    private List<AddressData> addresses;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<AddressData> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressData> addresses) {
		this.addresses = addresses;
	}
}
