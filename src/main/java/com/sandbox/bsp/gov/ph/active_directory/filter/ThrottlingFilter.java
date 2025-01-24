package com.sandbox.bsp.gov.ph.active_directory.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import ch.qos.logback.core.status.ErrorStatus;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/*
 * @author Ronald Olea
 */
//@Component
@Slf4j
public class ThrottlingFilter extends OncePerRequestFilter {

	@Value("${bucket4j.name}")
	private String bucketName;

	@Value("${bucket4j.limit}")
	private int rateLimit;

	@Value("${bucket4j.refill}")
	private int rateRefill;

	private Bucket createNewBucket() {
		// Creating and returning a rate-limiting bucket configuration (logic, settings
		// capacity and refill rate).
		return Bucket.builder()
				.addLimit(limit -> limit.capacity(rateLimit).refillGreedy(rateLimit, Duration.ofMinutes(rateRefill)))
				// Used to prevent abuse or malicious behavior by limiting the rate of request.
				//.addLimit(limit -> limit.capacity(5).refillGreedy(1, Duration.ofSeconds(1)))
				.build();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		// Get the remote address from the request.
		String clientKey = request.getRemoteAddr();
		log.info("Request from the remote address: " + clientKey);
		// Try to retrieve the bucket for this client.
		Bucket bucket = (Bucket) session.getAttribute(bucketName + "-" + clientKey);
		// If no bucket exists, create a new one and store it on the session.
		if (bucket == null) {
			bucket = createNewBucket();
			session.setAttribute(bucketName + "-" + clientKey, bucket);
		}

		// Try to consume one token from the bucket.
		ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
		// Check if there are no token available for this client.
		if (!consumptionProbe.isConsumed()) {
			log.info("Maximum request for " + clientKey + " already consumed. Remaining time to refill: "
					+ TimeUnit.NANOSECONDS.toSeconds(consumptionProbe.getNanosToWaitForRefill()));
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.getWriter().append("Too many requests. Please try again later.");
			return;
		}
		// Continue with the request if there has available token.
		log.info("The remaining Token for " + clientKey + ": " + consumptionProbe.getRemainingTokens());
		filterChain.doFilter(request, response);

	}

	/**
	 * TODO: Check for the request IP address first.
	 * 
	 * @param request
	 * @return
	 */
	public String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");

		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}

		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}

		// If the request is coming through an IPv6 loopback, convert it to IPv4
		if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
			ipAddress = "127.0.0.1";
		}

		return ipAddress;
	}

}
