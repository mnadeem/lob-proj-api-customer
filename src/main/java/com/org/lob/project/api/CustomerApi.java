package com.org.lob.project.api;

import static com.org.lob.support.Constants.PATH_VARIABLE_ID;
import static com.org.lob.support.Constants.REQUEST_MAPPING_CUSTOMER;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_NUMBER;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_SIZE;
import static com.org.lob.support.Constants.REQUEST_PARAM_SORT_BY;
import static com.org.lob.support.Constants.REQUEST_PARAM_SORT_DIRECTION;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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

import com.org.lob.project.service.CustomerService;
import com.org.lob.project.service.model.CustomerModel;
import com.org.lob.project.service.model.CustomerSearchRequest;

@RestController
@RequestMapping(REQUEST_MAPPING_CUSTOMER)
public class CustomerApi {
	
	private static final String SORT_DIRECTION_ASC = "asc";

	private final CustomerService customerService;

	public CustomerApi(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerModel> getCustomerDetail(@PathVariable(name = PATH_VARIABLE_ID) @Length(min = 1) @Positive Long customerId) {
		return ResponseEntity.ok(customerService.getCustomerById(customerId));
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCustomers(
			@RequestParam(name = REQUEST_PARAM_PAGE_NUMBER, required = true) @NotBlank(message = "{page_number.not_empty}") @Positive Integer pageNumber,
			@RequestParam(name = REQUEST_PARAM_PAGE_SIZE, required = true) @Positive Integer pageSize) {

		Page<CustomerModel> page = getCustomersPage(pageNumber, pageSize);
		return ResponseEntity.ok(page);
	}

	//http://localhost:8091/api/v1/customer/search?pageNumber=0&pageSize=10&zipCode=444
	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CustomerModel>> searchCustomers(@RequestParam Map<String, String> allRequestParams,
			@RequestParam(name = REQUEST_PARAM_PAGE_NUMBER, required = true) @Positive Integer pageNumber,
			@RequestParam(name = REQUEST_PARAM_PAGE_SIZE, required = true) @Positive Integer pageSize,
			@RequestParam(name = REQUEST_PARAM_SORT_BY, required = false) String sortBy,
			@RequestParam(name = REQUEST_PARAM_SORT_DIRECTION, required = false) String sortDirection) {

		CustomerSearchRequest searchRequest = new CustomerSearchRequest(allRequestParams);
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortBy, sortDirection);
		Page<CustomerModel> page = customerService.search(searchRequest, pageRequest);
		return ResponseEntity.ok(page);
	}
	
	private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
		if (StringUtils.hasText(sortBy)) {
			Direction direction = StringUtils.hasText(sortDirection) && SORT_DIRECTION_ASC.equalsIgnoreCase(sortDirection.trim())
					? Direction.ASC
					: Direction.DESC;
			return PageRequest.of(pageNumber, pageSize, Sort.by(new Order(direction, sortBy)));
		} else {
			return PageRequest.of(pageNumber, pageSize);
		}
	}

	private Page<CustomerModel> getCustomersPage(Integer pageNumber, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
		return customerService.findAll(pageRequest);
	}

	@PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerModel customerModel, UriComponentsBuilder ucBuilder) {

		CustomerModel createdCustomer = customerService.create(customerModel);

		return ResponseEntity
				.created(ucBuilder.path(REQUEST_MAPPING_CUSTOMER + "/{id}" ).buildAndExpand(createdCustomer.getId()).toUri())
				.body(createdCustomer);
	}

	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCustomer(
			@PathVariable(name = PATH_VARIABLE_ID) @NotBlank(message = "{id.not_empty}") @Length(min = 1) @Positive Long customerId,
			@RequestBody CustomerModel customerModel) {

		customerModel.setId(customerId);
		CustomerModel updatedCustomer = customerService.update(customerModel);
		return ResponseEntity.ok(updatedCustomer);
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> deleteCustomer(
			@PathVariable(name = PATH_VARIABLE_ID) @NotBlank(message = "{id.not_empty}") @Length(min = 1) @Positive Long customerId) {

		customerService.deleteCustomer(customerId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
