package com.auxime.contract.exception;

public class PortageConventionException extends Exception {
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
	public PortageConventionException(String message) {
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
