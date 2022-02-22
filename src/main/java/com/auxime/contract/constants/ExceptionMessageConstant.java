package com.auxime.contract.constants;


/**
 * @author Nicolas
 * @version 1.0.0
 * @since 1.0.0
 */
public class ExceptionMessageConstant {
	
	private ExceptionMessageConstant() {
		 throw new IllegalStateException("Utility class");
	}
	
	// CAPE Exception
	public static final String CAPE_NOT_FOUND = "This CAPE is not in DB";
	public static final String CAPE_FOUND = "This CAPE is already in DB";
	

}
