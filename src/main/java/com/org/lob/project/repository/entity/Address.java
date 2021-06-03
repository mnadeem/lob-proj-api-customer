package com.org.lob.project.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "PRJ_ADDRESS")
public class Address extends Auditable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "STREET_ADDRESS")
    private String streetAddress;
	@Column(name = "CITY")
    private String city;
	@Column(name = "STATE_CODE")
    private String stateCode;
	@Column(name = "COUNTRY")
    private  String country;
	@Column(name = "ZIP_CODE")
    private String zipCode;

    @ManyToOne
    @JsonBackReference
    private Customer customer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", streetAddress=" + streetAddress + ", city=" + city + ", stateCode=" + stateCode
				+ ", country=" + country + ", zipCode=" + zipCode + ", customer=" + customer + "]";
	}
}
