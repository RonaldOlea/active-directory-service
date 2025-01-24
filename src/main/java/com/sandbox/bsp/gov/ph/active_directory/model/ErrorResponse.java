/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ronald Olea
 *
 */
@Getter
@Setter
public class ErrorResponse {

	private String message;
	private String error;
	private String details;
}
