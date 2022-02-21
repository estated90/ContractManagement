package com.auxime.contract.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * This method is used in the security configuration. It will return an
 * unauthorized error if the user do not have the authorization to perform a
 * call to a method.
 * </p>
 * 
 * @author Nicolas
 *
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LogManager.getLogger(JwtAuthEntryPoint.class);

	/**
	 * @param request that resulted in an <code>AuthenticationException</code>
	 * @param response so that the user agent can begin authentication
	 * @param e that caused the invocation
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {
		logger.info("Error while getting authorization");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String message;

		if (e.getCause() != null) {
			message = e.getCause().toString() + " " + e.getMessage();
		} else {
			message = e.getMessage();
		}

		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
		logger.info("Answering to user");
		response.getOutputStream().write(body);
	}
}
