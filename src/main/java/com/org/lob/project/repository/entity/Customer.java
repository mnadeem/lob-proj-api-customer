package com.org.lob.project.repository.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "PRJ_CUSTOMER")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Customer extends Auditable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "FIRST_NAME")
    private String firstName;

	@Column(name = "LAST_NAME")
    private String lastName;

	@NotBlank(message = "{email.notempty}")
	@Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Address.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonManagedReference
    @NotAudited
    private List<Address> addresses;

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

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		if (this.addresses == null) {
			this.addresses = new ArrayList<>();
		} else {
			this.addresses.clear();
		}
		this.addresses.addAll(addresses);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + ", addresses=" + addresses + "]";
	}
}
