package com.org.lob.project.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.org.lob.project.api.model.ErrorMessage;
import com.org.lob.project.api.model.ErrorMessages;
import com.org.lob.project.exception.ApplicationException;

/**
 * https://reflectoring.io/spring-boot-exception-handling/
 * Refer https://www.baeldung.com/exception-handling-for-rest-with-spring for
 * more details
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<ErrorMessage> handleApplicationException(ApplicationException ex, WebRequest request) {
        return getErrorMessage(ex);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.error(ex.getMessage(), ex);

        return ResponseEntity.badRequest()
                .body(new ErrorMessage(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(RuntimeException exception, WebRequest request) {

    	logger.error(exception.getMessage(), exception);

        return ResponseEntity.internalServerError().body(
                new ErrorMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

    	logger.error(ex.getMessage(), ex);

        ErrorMessages messages = new ErrorMessages();
        populateErrorMessage(ex, messages);
        return ResponseEntity.badRequest().body(messages);
    }

    private void populateErrorMessage(MethodArgumentNotValidException ex, ErrorMessages messages) {
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = error.getObjectName();
            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            }
            String errorMessage = error.getDefaultMessage();
            messages.add(fieldName, errorMessage);
        });
    }

    private ResponseEntity<ErrorMessage> getErrorMessage(ApplicationException ex) {

    	logger.error(ex.getMessage(), ex);

        ResponseEntity<ErrorMessage> response;
        switch (ex.getErrorCode()) {
            case DATA_EMPTY:
                //response = ResponseEntity.notFound().build();
            	response = ResponseEntity.badRequest().body(new ErrorMessage(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage()));
                break;
            case DATA_DUPLICATE:
            case DATA_INTEGRITY:
            case DATA_INVALID:
            case INVALID_OPERATION:
                response = ResponseEntity.badRequest()
                        .body(new ErrorMessage(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage()));
                break;
            case UNAUTHORIZED_ERROR:
                response = ResponseEntity.badRequest()
                        .body(new ErrorMessage(String.valueOf(HttpStatus.UNAUTHORIZED.value()), ex.getMessage()));
                break;
            default:
                response = ResponseEntity.internalServerError()
                        .body(new ErrorMessage(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage()));
        }
        return response;
    }
}