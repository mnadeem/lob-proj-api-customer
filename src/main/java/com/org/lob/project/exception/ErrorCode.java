package com.org.lob.project.exception;

//001 -- 020 DATA EXCEPTION
//021 -- 040 SERVICE EXCEPTION
//041 -- 050 Web Exception
//051 -- 060 MQ Exception
public enum ErrorCode {
    UNKONWN("000", "Unknown Error: %s"), 

    DATA_ERROR("001", "No record found: %s"),
	DATA_EMPTY("002", "No record found: %s"),
	DATA_DUPLICATE("003", "Duplicate record : %s"), 
	DATA_INTEGRITY("004", "Contraint violation for %s"),
	DATA_INVALID("005", "Invalid data %s"),
	FILE_NOT_FOUND("006", "No file found %s"),

	BUSINESS_ERROR("021", "Invalid State"),
	INVALID_STATE("022", "Invalid State"),
	INVALID_OPERATION("023", "Invalid Operation"),
	RULE_ENGINE("024", "Rule Engine"),

	WEB_ERROR("041", "Web Error"),
	SERVER_ERROR("042", "Server Error"),
	UNAUTHORIZED_ERROR("043", "Un Authorized Error"),

	MQ_ERROR("051", "Messaging Error"),
	MQ_PRODUCER("052", "Error Sending Message"),
	MQ_CONSUMER("052", "Error Receiving Message");

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
