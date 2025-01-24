/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ronald Olea
 */

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
public class BadRequestException extends RuntimeException {

	private final String message;
}

// @AllArgsConstructor
/**Lombok generates a constructor with a 'message' argument.
 * public BadRequestException(String message) {
        super(message);
        this.message = message;
    }
 */