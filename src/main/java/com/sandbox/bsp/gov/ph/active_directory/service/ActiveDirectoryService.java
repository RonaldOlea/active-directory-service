/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sandbox.bsp.gov.ph.active_directory.model.CredentialResponse;
import com.sandbox.bsp.gov.ph.active_directory.model.Credentials;
import com.sandbox.bsp.gov.ph.active_directory.model.DirectoryContext;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ronald Olea
 */
@Service
@Slf4j
public class ActiveDirectoryService {

	@Autowired
	private ApiKeyService apiKeyService;

	private DirectoryContext directoryContext;

	@Value("${adint.host}")
	private String host;

	@Value("${adint.port}")
	private String port;

	@Value("${adint.domain}")
	private String domain;

	@Value("${dir.context.provider}")
	private String provider;

	@PostConstruct
	private void init() {
		try {
			directoryContext = new DirectoryContext();
			directoryContext.setHost(host);
			directoryContext.setPort(port);
			directoryContext.setDomain(domain);
			directoryContext.setProvider(provider);
		} catch (Exception e) {
			log.error("Unable to initialize the AD service. Please see stack trace");
			e.printStackTrace();
		}
	}

	public boolean authenticateV1(String username, String password) {
		// Initialize Credentials
		Credentials credentials = new Credentials();
		credentials.setSecurityPrincipal(username);
		credentials.setSecurityCredential(password);

		// Get DirectoryContext instance
		DirectoryContext context = this.directoryContext;
		log.info("Authenticating user " + credentials.getSecurityPrincipal() + " in AD host: "
				+ directoryContext.getHost() + " port: " + directoryContext.getPort() + " domain: "
				+ directoryContext.getDomain());

		// Attempt authentication
		CredentialResponse credResponse = new CredentialResponse();
		int authenticationResult = context.setContext(credentials);
		boolean isAuthenticated = false;

		// Handle authentication results
		if (authenticationResult == DirectoryContext.SUCCESS) {
			isAuthenticated = true;
		} else if (authenticationResult == DirectoryContext.FAIL) {
			isAuthenticated = false;
		}

		return isAuthenticated;
	}

	public CredentialResponse authenticate(String username, String password) {
		// Initialize Credentials
		Credentials credentials = new Credentials();
		credentials.setSecurityPrincipal(username);
		credentials.setSecurityCredential(password);

		// Get DirectoryContext instance
		DirectoryContext context = this.directoryContext;
		log.info("Authentication user {} in AD host: {} port: {} domain: {}", credentials.getSecurityPrincipal(),
				directoryContext.getHost(), directoryContext.getPort(), directoryContext.getDomain());

		// Attempt authentication
		CredentialResponse credResponse = new CredentialResponse();
		int authenticationResult = context.setContext(credentials);

		// Handle authentication results
		if (authenticationResult == DirectoryContext.SUCCESS) {
			credResponse.setMessage("Authentication successful.");
			credResponse.setSecurityApiKey(apiKeyService.setApiKey(credentials.getSecurityPrincipal()));
			credResponse.setAuthenticated(true);
		} else if (authenticationResult == DirectoryContext.FAIL) {
			credResponse.setMessage("Authentication failed. Invalid credentials.");
			credResponse.setAuthenticated(false);
		}

		return credResponse;
	}

}