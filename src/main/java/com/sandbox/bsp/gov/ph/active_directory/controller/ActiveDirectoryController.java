/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandbox.bsp.gov.ph.active_directory.exception.BadRequestException;
import com.sandbox.bsp.gov.ph.active_directory.model.CredentialRequest;
import com.sandbox.bsp.gov.ph.active_directory.model.CredentialResponse;
import com.sandbox.bsp.gov.ph.active_directory.service.ActiveDirectoryService;
import com.sandbox.bsp.gov.ph.active_directory.service.ApiKeyService;

/**
 * @author Ronald Olea
 */
@RestController
@RequestMapping("/api/authentication")
public class ActiveDirectoryController {

	@Autowired
	private ActiveDirectoryService adService;

	@Autowired
	private ApiKeyService apiKeyService;

	/**
	 * Handles the authentication request for a user.
	 * 
	 * @param credential
	 * @return 200 if the credential is valid, 401 otherwise
	 * @throws MissingServletRequestParameterException
	 * @throws BadRequestException
	 */
	@PostMapping("/login")
	private ResponseEntity<CredentialResponse> getAuthenticated(@RequestBody CredentialRequest credential) {
		String username = credential.getSecurityPrincipal();
		String password = credential.getSecurityCredential();

		if (username == null || username.isEmpty()) {
			throw new BadRequestException("Username");
		} else if (password == null || password.isEmpty()) {
			throw new BadRequestException("Password");
		} else {
			CredentialResponse response = adService.authenticate(username, password);
			if (response.isAuthenticated()) {
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new CredentialResponse(), HttpStatus.UNAUTHORIZED);
			}
		}
	}

	/**
	 * Handles the validation of API key.
	 * 
	 * @param credential
	 * @return
	 */
	@PostMapping("/validate")
	public ResponseEntity<Boolean> validateApiKey(@RequestBody CredentialRequest credential) {
		String username = credential.getSecurityPrincipal();
		String apiKey = credential.getSecurityApiKey();
		if (username == null || username.isEmpty()) {
			throw new BadRequestException("Username");
		} else if (apiKey == null || apiKey.isEmpty()) {
			throw new BadRequestException("API Key"); 
		} else {
			boolean response = apiKeyService.validateApiKey(username, apiKey);
			return new ResponseEntity<>(response, HttpStatus.OK);
//			if (response) {
//				return new ResponseEntity<>(response, HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//			}
		}
	}

	/**
	 * This is used for testing the API Key.
	 * 
	 * @return
	 */
	@GetMapping("/check-if-protected")
	public ResponseEntity<String> sayHello() {
		return new ResponseEntity<>("Hello, Access granted to the protected API!", HttpStatus.OK);
	}

	/**
	 * Handles the authentication request for a user.
	 * 
	 * @param credential
	 * @return true if the credential is valid, false otherwise
	 * @throws BadRequestException
	 * @throws MissingServletRequestParameterException
	 */

	// V1 API's
	@PostMapping("/loginV1")
	private boolean getAuthenticateV1(@RequestBody CredentialRequest credential)
			throws MissingServletRequestParameterException {
		String username = credential.getSecurityPrincipal();
		String password = credential.getSecurityCredential();
		if (username.isEmpty()) {
			throw new MissingServletRequestParameterException("username", "String");
		} else if (password.isEmpty()) {
			throw new MissingServletRequestParameterException("password", "String");
		} else {
			return adService.authenticateV1(username, password);
		}
	}

	/**
	 * Handles the validation of API key.
	 * 
	 * @param credential
	 * @return
	 * @throws BadRequestException
	 */
	@PostMapping("/validateV1")
	public boolean validateApiKeyV1(@RequestBody CredentialRequest credential)
			throws MissingServletRequestParameterException {
		String username = credential.getSecurityPrincipal();
		String apiKey = credential.getSecurityApiKey();
		if (username.isEmpty()) {
			throw new MissingServletRequestParameterException("username", "String");
		} else if (apiKey.isEmpty()) {
			throw new MissingServletRequestParameterException("api key", "String");
		} else {
			return apiKeyService.validateApiKey(username, apiKey);
		}
	}
}
