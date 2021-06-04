package com.org.lob.project.batch.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
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

	public static void main(String[] args) throws Exception {
		
		CustomerData customerData = new CustomerData();
		customerData.setEmailAddress("email");
		customerData.setFirstName("fiors");
		customerData.setLastName("last");
		
		AddressData ad = new AddressData();
		ad.setCity("city");
		ad.setCountry("copuntyhr");
		
		AddressData ad1 = new AddressData();
		ad1.setCity("city");
		ad1.setCountry("copuntyhr");
		
		customerData.setAddresses(Arrays.asList(ad, ad1));
		
		// create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(CustomerData.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to System.out
        m.marshal(customerData, System.out);

	}
}
