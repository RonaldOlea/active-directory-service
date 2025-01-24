/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandbox.bsp.gov.ph.active_directory.model.ApiKeyManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ronald Olea
 */
@Service
@Slf4j
public class ApiKeyService {

	@Autowired
	private ApiKeyManager apiKeyManager;

	/**
	 * Validate the provided API key against the valid API key from properties.
	 * 
	 * @param apiKey The API key sent by the client.
	 * @return true if valid, false otherwise.
	 */
	public boolean validateApiKey(String username, String apiKey) {
		if (!apiKeyManager.getCredentialForApiKey(username, apiKey).isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Create new API key credential.
	 * 
	 * @param username
	 */
	public String setApiKey(String username) {
		String newApiKey = UUID.randomUUID().toString();
		apiKeyManager.setCredentialForApiKey(username, newApiKey);
		log.info("Generate API KEY: " + newApiKey + " for " + username);

		return newApiKey;
	}

}
