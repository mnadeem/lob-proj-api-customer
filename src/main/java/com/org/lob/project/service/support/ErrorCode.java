package com.org.lob.project.service.support;

// 001 -- 020 DATA EXCEPTION
// 021 -- 040 SERVICE EXCEPTION
public enum ErrorCode {
	DATA_EMPTY("001", "No record found: %s"), DATA_DUPLICATE("002", "Duplicate record : %s"), DATA_INTEGRITY("003", "Contraint violation for %s");

	private final String code;
	private final String messge;

	private ErrorCode(String code, String messge) {
		this.code = code;
		this.messge = messge;
	}

	public String getCode() {
		return code;
	}

	public String getMessge() {
		return messge;
	}

	public String getFormattedMessage(Object[] params) {
		return String.format(messge, params);
	}
}
