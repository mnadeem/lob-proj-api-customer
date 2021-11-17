package com.org.lob.project.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ErrorCode errorCode;

	public ApplicationException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ApplicationException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ApplicationException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public static ApplicationException errorCode(ErrorCode errorCode, String message) {
		return new ApplicationException(errorCode, message);
	}

	public static ApplicationException unknown(String message) {
		return new ApplicationException(ErrorCode.UNKONWN, message);
	}

	public static ApplicationException noRecordFound(String message) {
		return new ApplicationException(ErrorCode.DATA_EMPTY, message);
	}

	public static ApplicationException noRecordFound(String message, Throwable cause) {
		return new ApplicationException(ErrorCode.DATA_EMPTY, message, cause);
	}

	public static ApplicationException duplicateRecord(String message) {
		return new ApplicationException(ErrorCode.DATA_DUPLICATE, message);
	}

	public static ApplicationException duplicateRecord(String message, Throwable cause) {
		return new ApplicationException(ErrorCode.DATA_DUPLICATE, message, cause);
	}

	public static ApplicationException invalidRecord(String message) {
		return new ApplicationException(ErrorCode.DATA_INVALID, message);
	}
	
	public static ApplicationException accessViolation(String message) {
		return new ApplicationException(ErrorCode.DATA_INTEGRITY, message);
	}

	public static ApplicationException invalidState(String message) {
		return new ApplicationException(ErrorCode.INVALID_STATE, message);
	}

	public static ApplicationException invalidOperation(String message) {
		return new ApplicationException(ErrorCode.INVALID_OPERATION, message);
	}

	public static ApplicationException mqProducer(String message, Throwable cause) {
		return new ApplicationException(ErrorCode.MQ_PRODUCER, message, cause);
	}

	public static ApplicationException fileNotFound(String message) {
		return new ApplicationException(ErrorCode.FILE_NOT_FOUND, message);
	}

	public static ApplicationException fileNotFound(String message, Throwable cause) {
		return new ApplicationException(ErrorCode.FILE_NOT_FOUND, message, cause);
	}

	public static ApplicationException serverError(String message) {
		return new ApplicationException(ErrorCode.SERVER_ERROR, message);
	}

	public static ApplicationException serverError(String message, Throwable cause) {
		return new ApplicationException(ErrorCode.SERVER_ERROR, message, cause);
	}

	public static ApplicationException unAuthorized(String message) {
		return new ApplicationException(ErrorCode.UNAUTHORIZED_ERROR, message);
	}

	public static ApplicationException unAuthorized(String message, Throwable cause) {
		return new ApplicationException(ErrorCode.UNAUTHORIZED_ERROR, message, cause);
	}

	public static ApplicationException ruleEngine(String message) {
		return new ApplicationException(ErrorCode.RULE_ENGINE, message);
	}

	public static ApplicationException ruleEngine(String message, Throwable cause) {
		return new ApplicationException(ErrorCode.RULE_ENGINE, message, cause);
	}
}
