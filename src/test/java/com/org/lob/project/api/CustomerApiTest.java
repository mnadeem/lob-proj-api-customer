package com.org.lob.project.api;

import static com.org.lob.support.Constants.REQUEST_MAPPING_CUSTOMER;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_NUMBER;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_SIZE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.org.lob.project.exception.ApplicationException;
import com.org.lob.project.service.DefaultCustomerService;
import com.org.lob.project.service.model.CustomerModel;
import com.org.lob.support.security.jwt.JwtTokenFilter;

//https://spring.io/guides/gs/testing-web/
@WebMvcTest(CustomerApi.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("classpath:application-test.properties")
class CustomerApiTest {

	private static final String FIRST_NAME_NADEEM = "NADEEM";
	
	@MockBean
	private JwtTokenFilter JwtTokenFilter;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DefaultCustomerService customerService;

	@Test
	void getByIdShouldBeOk() throws Exception {
		Long id = 1L;
		when(customerService.getCustomerById(id)).thenReturn(Optional.of(customerModel(id)));

		this.mockMvc.perform(get(REQUEST_MAPPING_CUSTOMER + '/' + id).contentType(MediaType.APPLICATION_JSON_VALUE))
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
	        .andExpect(jsonPath("$.statusCode").value("500"));
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
	        .andExpect(jsonPath("$.statusCode").value("400"));
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
	        .andExpect(jsonPath("$.statusCode").value("400"));
	}

	private CustomerModel customerModel(Long id) {
		CustomerModel customerModel = new CustomerModel();
		customerModel.setId(id);
		customerModel.setFirstName(FIRST_NAME_NADEEM);
		return customerModel;
	}
}