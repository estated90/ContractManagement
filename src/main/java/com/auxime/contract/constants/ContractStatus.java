package com.auxime.contract.constants;

/**
 * @author Nicolas
 *
 */
public enum ContractStatus {

	/**
	 * When a contract is in draft and not yet send for validation
	 */
	DRAFT,
	/**
	 * When a contract needs a validation from the Auxime teams
	 */
	PENDING_VALIDATION,
	/**
	 * When a contract is validated
	 */
	VALIDATED,
	/**
	 * When a contract has been refused and cannot be modified anymore
	 */
	REFUSED,
	/**
	 * When a modification needs to be done
	 */
	MODIFICATION_REQUIRED

}
