package com.auxime.contract.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auxime.contract.constants.RoleName;

@ExtendWith(MockitoExtension.class)
class JwtAuthTokenFilterTest {

	@Mock
	private JwtUtils tokenProvider;
	private String uuid;
	private String bearer;
	@InjectMocks
	private JwtAuthTokenFilter tokenFilter;
	private Map<String, Object> claims;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		uuid = "99923936-c3d3-4637-bb77-3f421b9f7f82";
		bearer = "Bearer " + uuid;
		claims = new HashMap<>();
		claims.put("fistName", "fistName");
		claims.put("lastName", "lastName");
		List<RoleName> roles = new ArrayList<>();
		roles.add(RoleName.ROLE_USER);
		claims.put("roles", roles);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void givenAuthentication_whenInfosCorrect_thenSecurityContextAuthenticated() throws Exception {
		// GIVEN
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		// WHEN
		when(request.getHeader(anyString())).thenReturn(bearer);
		when(tokenProvider.validateJwtToken(anyString())).thenReturn(true);
		when(tokenProvider.getUserNameFromJwtToken(anyString())).thenReturn(UUID.fromString(uuid));
		when(tokenProvider.getBoby(anyString())).thenReturn(claims);
		// THEN
		tokenFilter.doFilterInternal(request, response, filterChain);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		assertNotNull(auth);
		assertEquals(uuid, auth.getPrincipal().toString());
		assertEquals(1, auth.getAuthorities().size());
		assertTrue(auth.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.toString())));
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	@Test
	void givenAuthentication_whenUserEmpty_thenSecurityContextEmpty() throws Exception {
		// GIVEN
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		// WHEN
		when(request.getHeader(anyString())).thenReturn(bearer);
		when(tokenProvider.validateJwtToken(anyString())).thenReturn(true);
		when(tokenProvider.getUserNameFromJwtToken(anyString())).thenReturn(null);
		when(tokenProvider.getBoby(anyString())).thenReturn(claims);
		// THEN
		tokenFilter.doFilterInternal(request, response, filterChain);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		assertNull(auth);
	}
	
	@Test
	void givenAuthentication_whenTokenFalse_thenSecurityContextEmpty() throws Exception {
		// GIVEN
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		// WHEN
		when(request.getHeader(anyString())).thenReturn(bearer);
		when(tokenProvider.validateJwtToken(anyString())).thenReturn(false);
		// THEN
		tokenFilter.doFilterInternal(request, response, filterChain);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		assertNull(auth);
	}
	
	@Test
	void givenAuthentication_whenAuthNoBearer_thenSecurityContextEmpty() throws Exception {
		// GIVEN
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		// WHEN
		bearer = uuid;
		when(request.getHeader(anyString())).thenReturn(bearer);
		// THEN
		tokenFilter.doFilterInternal(request, response, filterChain);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		assertNull(auth);
	}

}
