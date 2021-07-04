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
}
