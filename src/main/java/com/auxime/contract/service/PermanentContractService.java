package com.auxime.contract.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.dto.permanent.CreatePermanentAmendment;
import com.auxime.contract.dto.permanent.PermanentCreate;
import com.auxime.contract.dto.permanent.PermanentUpdate;
import com.auxime.contract.exception.PermanentContractException;
import com.auxime.contract.model.PermanentContract;

/**
 * @author Nicolas
 *
 */
public interface PermanentContractService {

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of PermanentContract
	 */
	Map<String, Object> getAllPermanentContract(int page, int size);

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param accountId The the contract ID to look the amendment linked to.
	 * @return The list of Commercial Contract amendment
	 */
	Map<String, Object> getAllPermanentContractFromAccount(int page, int size, UUID accountId);

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	Optional<PermanentContract> getContractById(UUID contractId);

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new created contract object will be returned
	 * @throws PermanentContractException When an error is detected
	 */
	PermanentContract createNewContract(PermanentCreate contractPublic) throws PermanentContractException;

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws PermanentContractException When an error is detected
	 */
	PermanentContract updateContractFromId(PermanentUpdate contractPublic) throws PermanentContractException;

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws PermanentContractException When an error is raised if not found
	 */
	void deleteContract(PermanentUpdate contractPublic) throws PermanentContractException;

	/**
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Portage Convention the created object
	 * @throws PermanentContractException When an error is thrown during the process
	 */
	PermanentContract createPermanentContractAmendment(CreatePermanentAmendment contractPublic)
			throws PermanentContractException;

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return The list of Commercial Permanent amendment
	 */
	Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId);

}