package com.org.lob.project.service.support;

public class ProjectException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ErrorCode errorCode;
	private final String[] params;

	public ProjectException(ErrorCode errorCode, String[] params) {
		this.errorCode = errorCode;
		this.params = params;
	}

	public ProjectException(ErrorCode errorCode, String message, String[] params) {
		super(message);
		this.errorCode = errorCode;
		this.params = params;
	}

	public ProjectException(ErrorCode errorCode, String message, Throwable cause, String[] params) {
		super(message, cause);
		this.errorCode = errorCode;
		this.params = params;
	}

	public ProjectException(ErrorCode errorCode, Throwable cause, String[] params) {
		super(cause);
		this.errorCode = errorCode;
		this.params = params;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	@Override
	public String getMessage() {
		return getErrorCode().getFormattedMessage(params);
	}

	public static ProjectException noRecordFound(String message, String[] params) {
		return new ProjectException(ErrorCode.DATA_EMPTY, message, params);
	}

	public static ProjectException noRecordFound(String message, Throwable cause, String[] params) {
		return new ProjectException(ErrorCode.DATA_EMPTY, message, cause, params);
	}

	public static ProjectException duplicateRecord(String message, String[] params) {
		return new ProjectException(ErrorCode.DATA_DUPLICATE, message, params);
	}

	public static ProjectException duplicateRecord(String message, Throwable cause, String[] params) {
		return new ProjectException(ErrorCode.DATA_DUPLICATE, message, cause, params);
	}
}
