package com.org.lob.project.api;

import static com.org.lob.support.Constants.PATH_VARIABLE_ID;
import static com.org.lob.support.Constants.REQUEST_MAPPING_CUSTOMER;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_NUMBER;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.org.lob.project.api.model.ErrorMessage;
import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.DefaultCustomerService;

@RestController
@RequestMapping(REQUEST_MAPPING_CUSTOMER)
public class CustomerApi {

	private final DefaultCustomerService customerService;

	public CustomerApi(DefaultCustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCustomerDetail(
			@PathVariable(name = PATH_VARIABLE_ID) @Length(min = 1) @Positive Long customerId) {
		Optional<Customer> customer = getCustomerById(customerId);
		if (!customer.isPresent()) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(customer.get());
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCustomers(
			@RequestParam(name = REQUEST_PARAM_PAGE_NUMBER, required = true) @NotBlank(message = "{page_number.not_empty}") @Positive Integer pageNumber,
			@RequestParam(name = REQUEST_PARAM_PAGE_NUMBER, required = true) @Positive Integer pageSize) {
		try {
			Page<Customer> page = getCustomersPage(pageNumber, pageSize);
			return ResponseEntity.ok(page.getContent());
		} catch (Exception ex) {
			return handleException(ex);
		}
	}

	private Page<Customer> getCustomersPage(Integer pageNumber, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
		Page<Customer> page = customerService.findAll(pageRequest);
		return page;
	}

	@PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
		try {

			if (customer.getId() != null) {
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}

			Customer createdCustomer = customerService.create(customer);

			return ResponseEntity
					.created(ucBuilder.path(REQUEST_MAPPING_CUSTOMER).buildAndExpand(createdCustomer.getId()).toUri())
					.body(createdCustomer);
		} catch (Exception ex) {
			return handleException(ex);
		}
	}

	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCustomer(
			@PathVariable(name = PATH_VARIABLE_ID) @NotBlank(message = "{id.not_empty}") @Length(min = 1) @Positive Long customerId,
			@RequestBody Customer customer) {
		try {
			Optional<Customer> customerOptional = getCustomerById(customerId);
			if (!customerOptional.isPresent()) {
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
			customer.setId(customerId);
			Customer updatedCustomer = customerService.update(customer);
			return ResponseEntity.ok(updatedCustomer);
		} catch (Exception ex) {
			return handleException(ex);
		}
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> deleteCustomer(
			@PathVariable(name = PATH_VARIABLE_ID) @NotBlank(message = "{id.not_empty}") @Length(min = 1) @Positive Long customerId) {
		Optional<Customer> customer = getCustomerById(customerId);
		if (!customer.isPresent()) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		customerService.deleteCustomer(customerId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private Optional<Customer> getCustomerById(Long customerId) {
		return customerService.getCustomerById(customerId);
	}

	private ResponseEntity<ErrorMessage> handleException(Exception ex) {
		ex.printStackTrace();
		ErrorMessage error = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
}
