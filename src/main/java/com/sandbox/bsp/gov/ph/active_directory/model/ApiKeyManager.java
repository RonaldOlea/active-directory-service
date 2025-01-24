/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.model;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * @author Ronald Olea
 */

@Component
public class ApiKeyManager {

	private final ConcurrentHashMap<String, String> apiKeyManager = new ConcurrentHashMap<>();

	public boolean isApiKeyValid(String apiKey) {
		return apiKeyManager.contains(apiKey);
	}

	public String getCredentialForApiKey(String username, String apiKey) {
		String mapApiKey = apiKeyManager.get(username);
		if(mapApiKey != null && mapApiKey.equals(apiKey)) {
			return mapApiKey;
		}
		
		return "";
	}

	public void setCredentialForApiKey(String username, String apiKey) {
		apiKeyManager.put(username, apiKey);
	}

}
