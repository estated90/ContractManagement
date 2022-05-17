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
	public static final String COMMERCIAL_CONTRACT_FOUND = "This commercial contract is already in DB";
	public static final String COMMERCIAL_CONTRACT_NOT_FOUND = "This commercial contract is not in DB";
	public static final String COMMERCIAL_CONTRACT_NO_VALIDATOR = "No Validator has been found. Please contact the admin";
	public static final String COMMERCIAL_CONTRACT_INTERNAL_ERROR = "Internal error detected";
	public static final String PORTAGE_CONVENTION_FOUND = "This portage convention is already in DB";
	public static final String PORTAGE_CONVENTION_NOT_FOUND = "This portage convention is not in DB";
	public static final String PERMANENT_CONTRACT_FOUND = "This permanent contract is already in DB";
	public static final String PERMANENT_CONTRACT_NOT_FOUND = "This commercial contract is not in DB";
	public static final String TEMPORARY_CONTRACT_FOUND = "This temporary contract is already in DB";
	public static final String TEMPORARY_CONTRACT_NOT_FOUND = "This temporary contract is not in DB";
	public static final String PROFILE_NOT_RETRIEVED = "No Profile returned for that account";
	public static final String MODEL_NOT_FOUND = "The model of the document was not found";
	public static final String PATH_NOT_FOUND = "The saving path of the document was not found";
	public static final String CLOSE_DOC_ERROR = "The closing of the workbooks failed";
	public static final String CONVERTION_ERROR = "Error while converting the Document to PDF";
	public static final String READING_ERROR = "Error while reading the Document";

}
