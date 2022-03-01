package com.auxime.contract.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 
 * This service zill be used on all the feign request to the other
 * microservices. It will add the authorization header.
 * 
 * @version 1.01
 * @author Nicolas
 *
 */
@Component
public class FeignClientInterceptor implements RequestInterceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_TYPE = "Bearer";
	@Autowired
	private HttpServletRequest request;

	/**
	 * Apply method overriding. It transfer the authorization received by API to the
	 * other microservice. It will allow to authorize the request.
	 * 
	 * @param requestTemplate to intercept
	 */
	@Override
	public void apply(RequestTemplate requestTemplate) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			String authHeader = request.getHeader(AUTHORIZATION_HEADER);
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				requestTemplate.header(AUTHORIZATION_HEADER,
						String.format("%s %s", TOKEN_TYPE, authHeader.replace("Bearer ", "")));
			}
		}
	}
}
