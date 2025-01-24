package com.sandbox.bsp.gov.ph.active_directory.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ronald Olea
 */
@Getter
@Setter
public class CredentialRequest {

	private String securityPrincipal;
	private String securityCredential;
	private String securityApiKey;
}
