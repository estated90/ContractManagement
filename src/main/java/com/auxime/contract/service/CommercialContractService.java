package com.auxime.contract.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialUpdate;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.exception.CommercialContractException;
import com.auxime.contract.model.CommercialContract;

public interface CommercialContractService {

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of Cape
	 */
	List<CommercialContract> getAllCommercial();

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @return The list of Cape
	 */
	List<CommercialContract> getAllCommercialFromAccount(UUID accountId);

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param publicId The public ID is an UUID linked to the accounts of the users
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	Optional<CommercialContract> getCommercialById(UUID id);

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new updated contract object will be returned
	 * @throws CapeException When an error is detected
	 */
	CommercialContract createNewCommercial(CommercialCreate contractPublic) throws CommercialContractException;

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws CapeException When an error is detected
	 */
	CommercialContract updateCommercialFromId(CommercialUpdate contractPublic) throws CommercialContractException;

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws CapeException     When an error is raised if not found
	 * @throws ActivityException
	 */
	void deleteCommercial(CommercialUpdate contractPublic) throws CommercialContractException;

}