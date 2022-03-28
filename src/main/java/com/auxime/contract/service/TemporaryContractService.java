package com.auxime.contract.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.dto.temporary.CreateTemporaryAmendment;
import com.auxime.contract.dto.temporary.TemporaryCreate;
import com.auxime.contract.dto.temporary.TemporaryUpdate;
import com.auxime.contract.exception.TemporaryContractException;
import com.auxime.contract.model.TemporaryContract;

/**
 * @author Nicolas
 *
 */
public interface TemporaryContractService {

	/**
	 * Method to return all contract in DB
	 * 
	 * @param size
	 * @param page
	 * 
	 * @return The list of TemporaryContract
	 */
	List<TemporaryContract> getAllContract(int page, int size);

	/**
	 * Method to return all contract in DB from account
	 * 
	 * @param accountId The the contract ID to look the amendment linked to.
	 * @return The list of Temporary Contract amendment
	 */
	List<TemporaryContract> getAllContractFromAccount(int page, int size, UUID accountId);

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	Optional<TemporaryContract> getContractById(UUID contractId);

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new created contract object will be returned
	 * @throws TemporaryContractException When an error is detected
	 */
	TemporaryContract createNewContract(TemporaryCreate contractPublic) throws TemporaryContractException;

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws TemporaryContractException When an error is detected
	 */
	TemporaryContract updateContractFromId(TemporaryUpdate contractPublic) throws TemporaryContractException;

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws TemporaryContractException When an error is raised if not found
	 */
	void deleteContract(TemporaryUpdate contractPublic) throws TemporaryContractException;

	/**
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Temporary Contract the created object
	 * @throws TemporaryContractException When an error is thrown during the process
	 */
	TemporaryContract createTemporaryContractAmendment(CreateTemporaryAmendment contractPublic)
			throws TemporaryContractException;

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return The list of Temporary Contract amendment
	 */
	List<TemporaryContract> getAllAmendmentContract(int page, int size, UUID contractId);

}