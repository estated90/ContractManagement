package com.auxime.contract.jwt;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Service to create and test the token received
 * <p>
 * The services use JWT in order to create a token set in time and with an
 * account. It will validate the token and extract the account link to the token
 * </p>
 * 
 * @author Nicolas
 *
 */
@Component
public class JwtUtils {

	private static final Logger logger = LogManager.getLogger(JwtUtils.class);

	@Value("#{systemEnvironment['AUXIME_JWT_SECRET']}")
	private String jwtSecret;

	/**
	 * @param authToken Token provided in the authorized header
	 * @return Boolean to confirm if it is correct
	 * @throws SignatureException       The JWT signature is incorrect
	 * @throws MalformedJwtException    The JWT token is not correctly formed
	 * @throws ExpiredJwtException      The JWT token is expired
	 * @throws UnsupportedJwtException  The JWT token is unsupported
	 * @throws IllegalArgumentException The JWT token is empty
	 * 
	 */
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature -> Message: \r-----------{}-------------", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token -> Message: \r-----------{}-------------", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token -> Message: \r-----------{}-------------", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token -> Message: \r-----------{}-------------", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message: \r-----------{}-------------", e.getMessage());
		}
		return false;
	}

	/**
	 * @param token Token provided in the authorized header
	 * @return Return the user name of the user
	 */
	public UUID getUserNameFromJwtToken(String token) {
		return UUID.fromString(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject());
	}

	/**
	 * @param token Token provided in the authorized header
	 * @return the claims of the request
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRolesFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("roles", List.class);
	}
	
	/**
	 * @param jwtSecret the jwtSecret to set
	 */
	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}
}
