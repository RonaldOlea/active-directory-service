package com.sandbox.bsp.gov.ph.active_directory;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sandbox.bsp.gov.ph.active_directory.model.CredentialRequest;
import com.sandbox.bsp.gov.ph.active_directory.model.CredentialResponse;
import com.sandbox.bsp.gov.ph.active_directory.service.ActiveDirectoryService;

@SpringBootTest
@AutoConfigureMockMvc
public class ActiveDirectoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ActiveDirectoryService activeDirectoryService;

	private CredentialRequest user;

	@BeforeEach
	private void createSampleUser() {
		user = new CredentialRequest();
		user.setSecurityPrincipal("sample@gmail.com");
		user.setSecurityCredential("password");
	}

	@DisplayName("Login Test")
	@Test
	public void loginTest() throws Exception {
		CredentialResponse credentialResponse = new CredentialResponse();
		credentialResponse.setAuthenticated(true);
		credentialResponse.setMessage("Authentication successful.");

		when(activeDirectoryService.authenticate(user.getSecurityPrincipal(), user.getSecurityCredential()))
				.thenReturn(credentialResponse);

		StringBuilder builder = new StringBuilder();
		builder.append("{").append("\"securityPrincipal\": \"").append(user.getSecurityPrincipal()).append("\",")
				.append("\"securityCredential\": \"").append(user.getSecurityCredential()).append("\"").append("}");

		String requestBody = builder.toString();

		mockMvc.perform(post("/api/authentication/login").contentType(MediaType.APPLICATION_JSON).content(requestBody)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.authenticated").value(credentialResponse.isAuthenticated()))
				.andExpect(jsonPath("$.message").value(credentialResponse.getMessage()));
	}
}
