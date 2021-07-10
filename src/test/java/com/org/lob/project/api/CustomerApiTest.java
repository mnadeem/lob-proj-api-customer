package com.org.lob.project.api;

import static com.org.lob.support.Constants.REQUEST_MAPPING_CUSTOMER;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_NUMBER;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_SIZE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.lob.project.exception.ApplicationException;
import com.org.lob.project.service.DefaultCustomerService;
import com.org.lob.project.service.model.CustomerModel;
import com.org.lob.support.security.jwt.JwtTokenFilter;

//https://spring.io/guides/gs/testing-web/
@WebMvcTest(CustomerApi.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
class CustomerApiTest {

	private static final String BAD_REQUEST_ERROR = "400";
	private static final String INTERNAL_SERVER_ERROR = "500";

	private static final String FIRST_NAME_NADEEM = "NADEEM";
	
	@MockBean
	private JwtTokenFilter JwtTokenFilter;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private DefaultCustomerService customerService;

	@Test
	void getByIdShouldBeOk() throws Exception {
		Long id = 1L;
		when(customerService.getCustomerById(id)).thenReturn(Optional.of(customerModel(id)));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/' + id)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
		  		.andDo(print())
		  .andExpect(status().isOk())
		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.firstName").value(FIRST_NAME_NADEEM));
	}

	@Test
	void getByIdShouldBeNotFoundAndContentNotSet() throws Exception {
		Long id = 1L;
		when(customerService.getCustomerById(anyLong())).thenReturn(Optional.empty());

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/' + id)
					.contentType(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(print())
		        .andExpect(status().isNotFound())
		        .andExpect(content().string(""));
	}

	@Test
	void getAllCustomersIsOkay() throws Exception {
		
		int pageNumber = 0, pageSize = 10;

		when(customerService.findAll(PageRequest.of(pageNumber, pageSize))).thenReturn(new PageImpl<CustomerModel>(Collections.emptyList()));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/')
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
				.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
	        .andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$").isArray());
	}
	
	@Test
	void getAllCustomersIsInternalError() throws Exception {
		
		int pageNumber = 0, pageSize = 10;

		when(customerService.findAll(PageRequest.of(pageNumber, pageSize))).thenThrow(ApplicationException.unknown("Unknown Error"));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/')
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
				.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
	        .andDo(print())
	        .andExpect(status().isInternalServerError())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(INTERNAL_SERVER_ERROR));
	}

	@Test
	void getAllCustomersShouldBeDataErrorAndBadRequest() throws Exception {

		int pageNumber = 0, pageSize = 10;

		when(customerService.findAll(PageRequest.of(pageNumber, pageSize))).thenThrow(ApplicationException.duplicateRecord("Duplicate Record"));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/')
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
				.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
	        .andDo(print())
	        .andExpect(status().isBadRequest())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST_ERROR));
	}
	
	@Test
	void getAllCustomersShouldBeOtherErrorAndBadRequest() throws Exception {

		int pageNumber = 0, pageSize = 10;

		when(customerService.findAll(PageRequest.of(pageNumber, pageSize))).thenThrow(new RuntimeException());

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/')
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
				.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
	        .andDo(print())
	        .andExpect(status().isBadRequest())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST_ERROR));
	}

	@Test
	void searchCustomersIsInternalError() throws Exception {
		
		int pageNumber = 0, pageSize = 10;

		when(customerService.search(any(), any())).thenThrow(ApplicationException.unknown("Unknown Error"));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/' + "search")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
				.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
	        .andDo(print())
	        .andExpect(status().isInternalServerError())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(INTERNAL_SERVER_ERROR));
	}

	@Test
	void searchCustomersShouldBeDataErrorAndBadRequest() throws Exception {

		int pageNumber = 0, pageSize = 10;

		when(customerService.search(any(), any())).thenThrow(ApplicationException.duplicateRecord("Duplicate Record"));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/' + "search")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
				.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
	        .andDo(print())
	        .andExpect(status().isBadRequest())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST_ERROR));
	}

	@Test
	void searchCustomersShouldBeOtherErrorAndBadRequest() throws Exception {

		int pageNumber = 0, pageSize = 10;

		when(customerService.search(any(), any())).thenThrow(new RuntimeException());

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/' + "search")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
				.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
	        .andDo(print())
	        .andExpect(status().isBadRequest())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(BAD_REQUEST_ERROR));
	}

	@Test
	void searchCustomersShouldBeOk() throws Exception {

		int pageNumber = 0, pageSize = 10;

		when(customerService.search(any(), any())).thenReturn(new PageImpl<CustomerModel>(Collections.emptyList()));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/' + "search")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.param(REQUEST_PARAM_PAGE_NUMBER, String.valueOf(pageNumber))
					.param(REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize)))
				.andDo(print())
	        	.andExpect(status().isOk())
	        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        	.andExpect(jsonPath("$").isArray());
	}

	@Test
	void createShouldResultConflict() throws Exception {	
		
		this.mockMvc.perform(post(REQUEST_MAPPING_CUSTOMER + '/')
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(customerModel(1L)))
					.accept(MediaType.APPLICATION_JSON))
			    .andExpect(status().isConflict());
	}
	
	@Test
	void createShouldResultBadRequest() throws Exception {	
		
		CustomerModel customerModel = newCustomerModel();
		customerModel.setEmailAddress(null);

		this.mockMvc.perform(post(REQUEST_MAPPING_CUSTOMER + '/')
					.contentType(MediaType.APPLICATION_JSON)
			    	.content(objectMapper.writeValueAsString(customerModel))
			    	.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.emailAddress").isNotEmpty());
	}

	@Test
	void shouldCreatCustomerSuccessfully() throws Exception {
		Long id = 1L;

		CustomerModel newCustomerModel = newCustomerModel();

		when(customerService.create(any())).thenReturn(customerModel(id, newCustomerModel));

		this.mockMvc.perform(post(REQUEST_MAPPING_CUSTOMER + '/')
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(newCustomerModel))
			    	.accept(MediaType.APPLICATION_JSON))
			    .andExpect(status().isCreated())
			    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			    .andExpect(jsonPath("$.id").value(id));
	}
	
	@Test
	void createShouldResultBadRequestAndDataError() throws Exception {

		CustomerModel newCustomerModel = newCustomerModel();

		String message = "Duplicate Record";
		when(customerService.create(any())).thenThrow(ApplicationException.duplicateRecord(message));

		this.mockMvc.perform(post(REQUEST_MAPPING_CUSTOMER + '/')
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(newCustomerModel))
					.accept(MediaType.APPLICATION_JSON))
	        	.andDo(print())
	        	.andExpect(status().isBadRequest())
	        	.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        	.andExpect(jsonPath("$.statusCode").value(BAD_REQUEST_ERROR))
	        	.andExpect(jsonPath("$.errorMessage").value(message));
	}

	@Test
	void updateShouldBeOk() throws Exception {
		Long id = 1L;
		CustomerModel customerModel = customerModel(id);
		when(customerService.getCustomerById(id)).thenReturn(Optional.of(customerModel));
		when(customerService.update(any())).thenReturn(customerModel);

		this.mockMvc.perform(put(REQUEST_MAPPING_CUSTOMER + '/' + id)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(customerModel))
		  		.accept(MediaType.APPLICATION_JSON))
		  .andDo(print())
		  .andExpect(status().isOk())
		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.firstName").value(FIRST_NAME_NADEEM));
	}

	@Test
	void updateShouldBeNotFound() throws Exception {
		Long id = 1L;
		CustomerModel customerModel = customerModel(id);
		when(customerService.getCustomerById(id)).thenReturn(Optional.empty());

		this.mockMvc.perform(put(REQUEST_MAPPING_CUSTOMER + '/' + id)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(customerModel))
		  		.accept(MediaType.APPLICATION_JSON))
		  .andDo(print())
		  .andExpect(status().isNotFound())
	      .andExpect(content().string(""));
	}
	
	@Test
	void updateIsInternalError() throws Exception {
		
		Long id = 1L;
		CustomerModel customerModel = customerModel(id);
		when(customerService.getCustomerById(id)).thenThrow(ApplicationException.unknown("Unknown Error"));

		this.mockMvc.perform(put(REQUEST_MAPPING_CUSTOMER + '/' + id)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(customerModel))
		  		.accept(MediaType.APPLICATION_JSON))
		    .andDo(print())
	        .andExpect(status().isInternalServerError())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(INTERNAL_SERVER_ERROR));
	}

	@Test
	void deleteShouldBeOk() throws Exception {
		Long id = 1L;
		CustomerModel customerModel = customerModel(id);
		when(customerService.getCustomerById(id)).thenReturn(Optional.of(customerModel));
		doNothing().when(customerService).deleteCustomer(id);

		this.mockMvc.perform(delete(REQUEST_MAPPING_CUSTOMER + '/' + id)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(customerModel))
		  		.accept(MediaType.APPLICATION_JSON))
		  .andDo(print())
		  .andExpect(status().isOk());
	}
	
	@Test
	void deleteShouldBeNotFound() throws Exception {
		Long id = 1L;
		CustomerModel customerModel = customerModel(id);
		when(customerService.getCustomerById(id)).thenReturn(Optional.empty());

		this.mockMvc.perform(delete(REQUEST_MAPPING_CUSTOMER + '/' + id)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(customerModel))
		  		.accept(MediaType.APPLICATION_JSON))
		  .andDo(print())
		  .andExpect(status().isNotFound())
	      .andExpect(content().string(""));
	}
	
	@Test
	void deleteIsInternalError() throws Exception {
		
		Long id = 1L;
		CustomerModel customerModel = customerModel(id);
		when(customerService.getCustomerById(id)).thenThrow(ApplicationException.unknown("Unknown Error"));

		this.mockMvc.perform(delete(REQUEST_MAPPING_CUSTOMER + '/' + id)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(customerModel))
		  		.accept(MediaType.APPLICATION_JSON))
		    .andDo(print())
	        .andExpect(status().isInternalServerError())
	        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	        .andExpect(jsonPath("$.statusCode").value(INTERNAL_SERVER_ERROR));
	}

	private CustomerModel newCustomerModel() {
		CustomerModel customerModel = new CustomerModel();
		customerModel.setFirstName(FIRST_NAME_NADEEM);
		customerModel.setEmailAddress("some@email.com");
		return customerModel;
	}

	private CustomerModel customerModel(Long id, CustomerModel customerModel) {
		CustomerModel newCust = new CustomerModel();
		newCust.setId(id);
		newCust.setFirstName(customerModel.getFirstName());
		newCust.setEmailAddress(customerModel.getEmailAddress());
		return newCust;
	}

	private CustomerModel customerModel(Long id) {
		return customerModel(id, newCustomerModel());
	}
}