package com.auxime.contract.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.auxime.contract.exception.CapeException;
import com.auxime.contract.exception.CommercialContractException;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.exception.PermanentContractException;
import com.auxime.contract.exception.PortageConventionException;
import com.auxime.contract.exception.TemporaryContractException;

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
	 * @return ResponseEntity Object Return the APIError answer
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleApiException(AccessDeniedException ex) {
		final var apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(),
				"You do not have the rigths to use this API");
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
	
	/**
	 * Method to manage all CapeException exceptions
	 * 
	 * @param ex Exception thrown by a method
	 * @return ResponseEntity Object Return the APIError answer
	 */
	@ExceptionHandler(CapeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(
    		CapeException ex) {
    	final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Bad request, unable to perform request");
    	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
	/**
	 * Method to manage all CommercialContractException exceptions
	 * 
	 * @param ex Exception thrown by a method
	 * @return ResponseEntity Object Return the APIError answer
	 */
	@ExceptionHandler(CommercialContractException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(
    		CommercialContractException ex) {
    	final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Bad request, unable to perform request");
    	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    } 
	/**
	 * Method to manage all PermanentContract exceptions
	 * 
	 * @param ex Exception thrown by a method
	 * @return ResponseEntity Object Return the APIError answer
	 */
	@ExceptionHandler(PermanentContractException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(
    		PermanentContractException ex) {
    	final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Bad request, unable to perform request");
    	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    } 
	/**
	 * Method to manage all PortageConventionException exceptions
	 * 
	 * @param ex Exception thrown by a method
	 * @return ResponseEntity Object Return the APIError answer
	 */
	@ExceptionHandler(PortageConventionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(
    		PortageConventionException ex) {
    	final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Bad request, unable to perform request");
    	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    } 
	/**
	 * Method to manage all CapeException exceptions
	 * 
	 * @param ex Exception thrown by a method
	 * @return ResponseEntity Object Return the APIError answer
	 */
	@ExceptionHandler(TemporaryContractException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(
    		TemporaryContractException ex) {
    	final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Bad request, unable to perform request");
    	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    } 
	
	/**
	 * Method to manage all CapeException exceptions
	 * 
	 * @param ex Exception thrown by a method
	 * @return ResponseEntity Object Return the APIError answer
	 */
	@ExceptionHandler(PdfGeneratorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleApiException(
    		PdfGeneratorException ex) {
    	final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Bad request, unable to perform request");
    	return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    } 

}
