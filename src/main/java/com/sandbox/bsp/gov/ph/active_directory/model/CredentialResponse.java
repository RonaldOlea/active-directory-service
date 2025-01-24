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
public class CredentialResponse {

	private String message;
	private String securityApiKey;
	private boolean isAuthenticated;
}
