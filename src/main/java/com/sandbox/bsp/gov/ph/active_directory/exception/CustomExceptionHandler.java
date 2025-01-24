/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sandbox.bsp.gov.ph.active_directory.model.ErrorResponse;

/**
 * @author Ronald Olea
 *
 */
@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleMissingParams(BadRequestException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("Bad Request");
		errorResponse.setError("Missing required parameter: " + ex.getMessage());
		errorResponse.setDetails("Please check the request parameters.");

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("Internal Server Error");
		errorResponse.setError("An unexpected error occurred.");
		errorResponse.setDetails(ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
