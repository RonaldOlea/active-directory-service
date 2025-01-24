/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Ronald Olea
 */
@RestControllerAdvice
public class ActiveDirectoryExceptionHandler {

	// Handle MissingServletRequestParameterException
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParameter(MissingServletRequestParameterException e) {
		String message = "Missing required request parameter: " + e.getParameterName();
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
}
