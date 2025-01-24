/**
 * 
 */
package com.sandbox.bsp.gov.ph.active_directory.model;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ronald Olea
 */
@Getter
@Setter
@Slf4j
public class DirectoryContext {

	private static final String CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	private static final String SECURITY_AUTH = "simple";
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;

	private String host;
	private String port;
	private String domain;
	private String provider;

	public int setContext(Credentials credentials) {
		// Logic to authenticate user with Active Directory using the credentials
		// This could involve creating an LDAP connection and binding it with provided
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.PROVIDER_URL, provider);
		env.put(Context.INITIAL_CONTEXT_FACTORY, CONTEXT_FACTORY);
		env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTH);
		env.put(Context.SECURITY_PRINCIPAL, credentials.getSecurityPrincipal());
		env.put(Context.SECURITY_CREDENTIALS, credentials.getSecurityCredential());

		// Declare the InitialDirContext variable
		InitialDirContext dirContext = null;

		try {
			// Create a InitialDirContext to authenticate the user.
			dirContext = new InitialDirContext(env);
			log.info("Authentication was successful for user {}.", credentials.getSecurityPrincipal());
			return SUCCESS;
		} catch (NamingException e) {
			// If an error occurs during authentication (e.g. invalid credentials code 49)
			log.error(
					"Authentication failed for user {}. Invalid credentials.", credentials.getSecurityPrincipal());
			return FAIL;
		} finally {
			if (dirContext != null) {
				try {
					//Close the InitialDirContext after using it. To release any resources associated with the connection.
					dirContext.close();
					log.info("LDAP context closed successfully.");
				} catch (NamingException e) {
					log.error("Failed to close the LDAP context for user {}", credentials.getSecurityPrincipal(), e);
				}
			}
		}
	}
}
