package com.auxime.contract.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.dto.CommentCommercialPublic;
import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialUpdate;
import com.auxime.contract.dto.commercial.CreateCommercialAmendment;
import com.auxime.contract.exception.CommercialContractException;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.enums.PortageCompanies;

/**
 * @author Nicolas
 *
 */
public interface CommercialContractService {

	/**
	 * Method to return all contract in DB
	 * @param contractStatus
	 * 
	 * @return The list of Commercial Contract
	 */
	Map<String, Object> getAllCommercial(int page, int size, String filter, LocalDate startDate, LocalDate endDate,
	ContractState contractState, PortageCompanies structureContract, ContractStatus contractStatus);

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @param accountId ID of the account to extract the contract from
	 * @return The list of Commercial Contract
	 */
	Map<String, Object> getAllCommercialFromAccount(int page, int size, UUID accountId);

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId The public ID is an UUID linked to the accounts of the
	 *                   users
	 * @return The list of Commercial Contract amendment
	 */
	Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId);

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param contractId The public ID is an UUID linked to the accounts of the
	 *                   users
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	Optional<CommercialContract> getCommercialById(UUID contractId);

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new updated contract object will be returned
	 * @throws CommercialContractException When an error is detected
	 */
	CommercialContract createNewCommercial(CommercialCreate contractPublic) throws CommercialContractException;

	/**
	 * Create an addendum to a CAPE contract
	 * 
	 * @param contract The object contract with the fields mandatory
	 * @return CommercialContract Contract the created object
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	CommercialContract createAmendmentCommercial(CreateCommercialAmendment contract) throws CommercialContractException;

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws CommercialContractException When an error is detected
	 */
	CommercialContract updateCommercialFromId(CommercialUpdate contractPublic) throws CommercialContractException;

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws CommercialContractException When an error is raised if not found
	 */
	void deleteCommercial(UUID contractId) throws CommercialContractException;

	/**
	 * Ask for modification for a contract in DB.
	 * 
	 * @param contractId    The ID of the contract to change the status
	 * @param commentCreate The comment to ask the modification
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	CommercialContract modificationRequired(UUID contractId, CommentCommercialPublic commentCreate)
			throws CommercialContractException;

	/**
	 * Put a contract in waiting for validation by auxime.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	CommercialContract pendingValidationContract(UUID contractId) throws CommercialContractException;

	/**
	 * Refuse a contract in DB.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	CommercialContract refuseContract(UUID contractId) throws CommercialContractException;

	/**
	 * Validate a contract in DB. Will generate the PDF.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	CommercialContract validateContract(UUID contractId) throws CommercialContractException;

	/**
	 * Adding a comment to answer.
	 * 
	 * @param contractId    The ID of the contract to change the status
	 * @param commentCreate The comment to ask the modification
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	CommercialContract commentingContract(UUID contractId, CommentCommercialPublic commentCreate)
			throws CommercialContractException;

}