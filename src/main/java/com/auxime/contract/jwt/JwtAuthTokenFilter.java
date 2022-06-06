package com.auxime.contract.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 
 * Filter base class that aims to guarantee a single execution per request
 * dispatch. It will manage the tokens.
 * <P>
 * The request manage all tokens verification. Using a single call to the
 * method. The objective is to extract the token from a request and set the
 * authorization.
 * </p>
 * 
 * @author Nicolas
 *
 */
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils tokenProvider;

	private static final Logger ToLog = LogManager.getLogger(JwtAuthTokenFilter.class);

	/**
	 * <p>
	 * Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 * </p>
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param filterChain current HTTP filter Chain
	 * @throws ServletException throw a servlet exception
	 * @throws IOException throw a exception when can not set authentication
	 * 
	 **/
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwt(request);
			if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
				UUID username = tokenProvider.getUserNameFromJwtToken(jwt);
				if (username != null) {
					List<String> roles = tokenProvider.getRolesFromJwtToken(jwt);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					roles.stream().forEach(role ->
						authorities.add(new SimpleGrantedAuthority(role))
					);
					// 5. Create auth object
					// UsernamePasswordAuthenticationToken: A built-in object, used by spring to
					// represent the current authenticated / being authenticated user.
					// It needs a list of authorities, which has type of GrantedAuthority interface,
					// where SimpleGrantedAuthority is an implementation of that interface
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							username, null
							,roles.stream().map(SimpleGrantedAuthority::new).toList());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception e) {
			ToLog.error("Can NOT set user authentication -> Message: ", e);
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * Function to get the token by removing the bearer part
	 * @param request current HTTP request
	 * @return token without bearer before
	 */
	private String getJwt(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.replace("Bearer ", "");
		}

		return null;
	}
}