package com.org.lob.project.service.support;

public class ProjectException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ErrorCode errorCode;

	public ProjectException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ProjectException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ProjectException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}


	public static ProjectException noRecordFound(String message) {
		return new ProjectException(ErrorCode.DATA_EMPTY, message);
	}

	public static ProjectException noRecordFound(String message, Throwable cause) {
		return new ProjectException(ErrorCode.DATA_EMPTY, message, cause);
	}

	public static ProjectException duplicateRecord(String message) {
		return new ProjectException(ErrorCode.DATA_DUPLICATE, message);
	}

	public static ProjectException duplicateRecord(String message, Throwable cause) {
		return new ProjectException(ErrorCode.DATA_DUPLICATE, message, cause);
	}
}
