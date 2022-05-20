package com.auxime.contract.exceptionhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Nico
 *         <p>
 *         Exception handler for global API exception. When a request is
 *         received on the controller, if any information or method used are
 *         incorrect, then the method will return the correct Exception.
 *         </p>
 *
 */
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// From this point the HttpStatus is :
	// 400

	/**
	 * <p>
	 * Exception to be thrown when validation on an argument annotated with @Valid
	 * fails. It will return the list of incorrect element as a json list.
	 * </p>
	 * 
	 * @param ex      Exception raised when the method arguments are incorrect
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName()); //
		final List<String> errors = new ArrayList<>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "An error occured while deserializing the data");
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * 
	 * <p>
	 * Exception to be thrown when there is a binding conflict, like a port
	 * conflict. It will return the list of incorrect element as a json list.
	 * 
	 * @param ex      Exception raised when the method arguments are incorrect
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName()); //
		final List<String> errors = new ArrayList<>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	/**
	 * 
	 * <p>
	 * A TypeMismatchException raised while resolving a controller method argument.
	 * It will return the list of incorrect element as a json list.
	 * 
	 * @param ex      Exception raised when the method arguments are incorrect
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName()); // final
		String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
				+ ex.getRequiredType();

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * 
	 * <p>
	 * Raised when the part of a "multipart/form-data" request identified by its
	 * name cannot be found. It will return the list of incorrect element as a json
	 * list.
	 * 
	 * @param ex      Exception raised when the method arguments are incorrect
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName()); //
		final String error = ex.getRequestPartName() + " part is missing";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * 
	 * <p>
	 * ServletRequestBindingException subclass that indicates a missing parameter.
	 * It will return the list of incorrect element as a json list.
	 * 
	 * @param ex      Exception raised when the method arguments are incorrect
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getParameterName() + " parameter is missing";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * <p>
	 * Exception that indicates that a method argument has not the expected type. It
	 * will raised an exception if it is not the correct type.
	 * </p>
	 * 
	 * @param ex      Exception raised when the type is mismatch
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		Class<?> requiredType = ex.getRequiredType();
		String nameType = null;
		if (requiredType == null) {
			nameType = "Unknown";
		} else {
			nameType = requiredType.getName();
		}
		final String error = ex.getName() + " should be of type " + nameType;

		final var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// 404
	/**
	 * <p>
	 * The DispatcherServlet can't find a handler for a request. It will raised an
	 * exception if it is not the correct type.
	 * </p>
	 * 
	 * @param ex      Exception raised when the type is mismatch
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// From this point the HttpStatus is :
	// 405
	/**
	 * <p>
	 * Exception thrown when a request handler does not support a specific request
	 * method.. It will send the possible correction as JSON in the message.
	 * </p>
	 * 
	 * @param ex      Exception raised when the HTTP method is incorrect
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final var builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		Set<HttpMethod> requiredHttp = ex.getSupportedHttpMethods();
		String response = null;
		if (requiredHttp == null) {
			response = "Unknown";
		} else {
			requiredHttp.forEach(t -> builder.append(t + " "));
			response = builder.toString();
		}
		final var apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), response);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// From this point the HttpStatus is : // 415
	/**
	 * <p>
	 * Exception thrown when a client POSTs, PUTs, or PATCHes content of a type not
	 * supported by request handler. It will send the possible correction as JSON in
	 * the message.
	 * </p>
	 * 
	 * @param ex      Exception raised when the media type is not supported
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		final var builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

		final var apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(),
				builder.substring(0, builder.length() - 2));
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// 500
	/**
	 * <p>
	 * This method will return the exception raised in the code by the application.
	 * </p>
	 * 
	 * @param ex      Exception raised when an issue occured in the application
	 * @param request The request received by the controller
	 * @return An answer with the error the type and the message
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
