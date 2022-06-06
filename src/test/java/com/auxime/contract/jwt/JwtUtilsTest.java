package com.auxime.contract.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auxime.contract.constants.RoleName;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

class JwtUtilsTest {

	private JwtUtils utils;
	private Date issuedAt;
	private Date expiration;
	private String uuid;
	private SignatureAlgorithm algorithm;
	private String key;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		utils = new JwtUtils();
		utils.setJwtSecret("thisIsTest");
		issuedAt = new Date();
		expiration = new Date((new Date()).getTime() + 1000 * 1000);
		uuid = "99923936-c3d3-4637-bb77-3f421b9f7f82";
		algorithm = SignatureAlgorithm.HS512;
		key = "thisIsTest";
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void givenAuthentication_whenVerifyingToken_thenReturnTrue() {
		assertTrue(utils.validateJwtToken(generateJwtToken()));
	}

	@Test
	void givenAuthenticationIncorrectSecretKey_whenVerifyingToken_thenReturnFalse() {
		utils.setJwtSecret("OtherSecret");
		assertFalse(utils.validateJwtToken(generateJwtToken()));
	}

	@Test
	void givenAuthenticationMalFormedToken_whenVerifyingToken_thenReturnFalse() {
		String original = generateJwtToken();
		String token = original.substring(2);
		assertFalse(utils.validateJwtToken(token));
	}
	
	@Test
	void givenAuthenticationExpired_whenVerifyingToken_thenReturnFalse() {
		issuedAt = new Date((new Date()).getTime() - 2000 * 1000);
		expiration = new Date((new Date()).getTime() - 1000 * 1000);
		assertFalse(utils.validateJwtToken(generateJwtToken()));
	}
	
	@Test
	void givenAuthenticationWrongAlgorithm_whenVerifyingToken_thenReturnFalse() {
		utils.setJwtSecret(null);
		assertFalse(utils.validateJwtToken(generateJwtToken()));
	}
	
	@Test
	void givenToken_whenGettingUser_thenReturnID() {
		UUID token = utils.getUserNameFromJwtToken(generateJwtToken());
		assertEquals(UUID.fromString(uuid), token);
	}
	
	@Test
	void givenToken_whenGettingBody_thenReturnClaims() {
		List<String> body = utils.getRolesFromJwtToken(generateJwtToken());
		assertTrue(body.contains("ROLE_USER"));
	}

	private String generateJwtToken() {
		List<String> roles = new ArrayList<>();
		roles.add(RoleName.ROLE_USER.toString());
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(uuid, null,
				roles.stream().map(SimpleGrantedAuthority::new).toList());
		return Jwts.builder()
				.claim("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
				.claim("firstName", "firstName")
				.claim("lastName", "lastName")
				.setSubject(uuid)
				.setIssuedAt(issuedAt)
				.setExpiration(expiration)
				.signWith(algorithm, key).compact();
	}

}
