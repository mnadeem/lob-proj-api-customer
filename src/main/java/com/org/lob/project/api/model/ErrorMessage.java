package com.org.lob.project.api.model;

public class ErrorMessage {
    private int statusCode;
    private String errorMessage;

	public ErrorMessage(int statusCode, String errorMessage) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}
	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}