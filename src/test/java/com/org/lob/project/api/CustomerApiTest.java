package com.org.lob.project.api;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.org.lob.project.service.DefaultCustomerService;
import com.org.lob.project.service.model.CustomerModel;
import com.org.lob.support.security.jwt.JwtTokenFilter;

//https://spring.io/guides/gs/testing-web/
@WebMvcTest(CustomerApi.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerApiTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JwtTokenFilter JwtTokenFilter;

	@MockBean
	private DefaultCustomerService customerService;

	@Test
	void greetingShouldReturnMessageFromService() throws Exception {
		when(customerService.getCustomerById(anyLong())).thenReturn(Optional.of(customerWithIdOne()));

		this.mockMvc.perform(get("/api/v1/customer/1").contentType(MediaType.APPLICATION_JSON_VALUE))
		  .andDo(print())
		  .andExpect(status().isOk())
		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		  .andExpect(jsonPath("$.firstName").value("john"));
	}

	private CustomerModel customerWithIdOne() {
		CustomerModel customerModel = new CustomerModel();
		customerModel.setId(1L);
		customerModel.setFirstName("john");
		return customerModel;
	}
}
