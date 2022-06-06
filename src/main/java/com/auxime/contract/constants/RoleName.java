package com.auxime.contract.constants;

/**
 * List of all the roles in the application
 * @author Nicolas
 *
 */
public enum RoleName {
    /**
     * Role for an internal user
     */
    ROLE_USER("USER"),
    ROLE_DEVELOPMENT("DEVELOPMENT"),
    ROLE_ADMIN("ADMIN"),
    ROLE_COUNSELOR("COUNSELOR"),
    ROLE_ACCOUNTANCY_MANAGER("ACCOUNTANCY_MANAGER"),
    ROLE_INVOICING("INVOICING"),
    ROLE_FSE("FSE"),
    ROLE_EXPENSES("EXPENSES"),
    ROLE_PAYROLL_MANAGER("PAYROLL_MANAGER"),
    ROLE_DIRECTOR("DIRECTOR"),
    ROLE_SHAREHOLDER("SHAREHOLDER"),
    ROLE_IT("IT"),
    ROLE_EXTERNAL_TRAINER("EXTERNAL_TRAINER"),
    ROLE_INTERNAL_TRAINER("INTERNAL_TRAINER"),
    ROLE_QUALIOPI("QUALIOPI"), 
    ROLE_APP("APP");
    
    private String roleString;
    
    /**
	 * @return the roleString
	 */
	public String getRoleString() {
		return roleString;
	}

	// enum constructor - cannot be public or protected
    private RoleName(String roleString) {
        this.roleString = roleString;
    }
    
}