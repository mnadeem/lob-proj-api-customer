package com.org.lob.project.service.model;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import static com.org.lob.support.Constants.*;
import static com.org.lob.support.Constants.REQUEST_PARAM_PAGE_SIZE;

public class CustomerSearchRequest {

    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final PageRequest pageRequest;

    public CustomerSearchRequest(Map<String, String> params) {
    	this.pageRequest = pageRequest(params);
    	this.firstName = params.get(REQUEST_PARAM_FIRST_NAME);
    	this.lastName = params.get(REQUEST_PARAM_LAST_NAME);
    	this.emailAddress = params.get(REQUEST_PARAM_EMAIL);
	}

    private PageRequest pageRequest(Map<String, String> params) {
    	String pageNumber = params.get(REQUEST_PARAM_PAGE_NUMBER);
    	String pageSize = params.get(REQUEST_PARAM_PAGE_SIZE);

    	return pageNumber != null && pageSize != null ? PageRequest.of(Integer.valueOf(pageNumber), Integer.valueOf(pageSize)) :null;
    }
    
    public boolean isFirstNameValid() {
    	return StringUtils.hasText(getFirstName());
    }
    
    public boolean isLastNameValid() {
    	return StringUtils.hasText(getLastName());
    }

    public boolean isPageRequestValid() {
    	return pageRequest != null;
    }

    public boolean isEmailValid() {
    	return StringUtils.hasText(getEmailAddress());
    }

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public PageRequest getPageRequest() {
		return pageRequest;
	}

	@Override
	public String toString() {
		return "CustomerSearchRequest [firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + ", pageRequest=" + pageRequest + "]";
	}
}
