package com.auxime.contract.exception;

/**
 * Exception linked to all action on the CAPE
 * @author Nicolas
 *
 */
public class ProxyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Message to return to the user
	 */
	private final String message;
	
	/**
	 * @param message to return
	 */
	public ProxyException(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}
}
