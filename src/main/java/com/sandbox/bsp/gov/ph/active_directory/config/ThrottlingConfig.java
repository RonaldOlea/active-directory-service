package com.sandbox.bsp.gov.ph.active_directory.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sandbox.bsp.gov.ph.active_directory.filter.ThrottlingFilter;

/**
 * @author Ronald Olea
 *
 */

@Configuration
public class ThrottlingConfig {

	@Bean
	public ThrottlingFilter throttlingFilter() {
		return new ThrottlingFilter();
	}

	// Register the ThrottlingFilter so it can be applied to API endpoints
	@Bean
	public FilterRegistrationBean<ThrottlingFilter> loggingFilter(ThrottlingFilter filter) {
		FilterRegistrationBean<ThrottlingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("/api/authentication/login"); // Apply the filter only to /login endpoint
		return registrationBean;
	}
}
