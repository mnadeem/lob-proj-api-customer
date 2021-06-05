package com.org.lob.project.batch.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerData {

	private Long id;
	private String firstName;
	private String lastName;
	private String emailAddress;

	@XmlElementWrapper(name = "addresses")
	@XmlElement(name = "address")
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
		if (this.addresses == null) {
			this.addresses = new ArrayList<>();
		} else {
			this.addresses.clear();
		}
		this.addresses.addAll(addresses);
	}

	@Override
	public String toString() {
		return "CustomerData [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + "]";
	}
}
