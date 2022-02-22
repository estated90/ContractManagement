package com.auxime.contract.exceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.auxime.contract.exception.CapeException;

/**
 * @author Nicolas
 * @version 1.0.0
 * @since 1.0.0
 *
 *        <p>
 *        Exception handler for custom exception
 *        </p>
 *
 */
@ControllerAdvice
public class ApiExceptionHandler {

	/**
	 * Method to manage all Accessdenied exceptions
	 * 
	 * @param ex Exception thrown by a method
	 * @return ResponseEntity<Object> Return the APIError answer
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleApiException(AccessDeniedException ex) {
		final var apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(),
				"You do not have the rigths to use this API");
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	@ExceptionHandler(CapeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(
    		CapeException ex) {
    	final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Bad request, unable to perform request");
    	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    } 

}
