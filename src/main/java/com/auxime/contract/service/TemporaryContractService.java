package com.auxime.contract.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.dto.temporary.CreateTemporaryAmendment;
import com.auxime.contract.dto.temporary.TemporaryCreate;
import com.auxime.contract.dto.temporary.TemporaryUpdate;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.exception.TemporaryContractException;
import com.auxime.contract.model.TemporaryContract;
import com.auxime.contract.model.enums.PortageCompanies;

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
	Map<String, Object> getAllContract(int page, int size, String filter, LocalDate startDate, LocalDate endDate,
	ContractState contractState, PortageCompanies structureContract);

	/**
	 * Method to return all contract in DB from account
	 * 
	 * @param accountId The the contract ID to look the amendment linked to.
	 * @return The list of Temporary Contract amendment
	 */
	Map<String, Object> getAllContractFromAccount(int page, int size, UUID accountId);

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
	 * @throws PdfGeneratorException
	 */
	TemporaryContract createNewContract(TemporaryCreate contractPublic) throws TemporaryContractException, PdfGeneratorException;

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
	void deleteContract(UUID contractId) throws TemporaryContractException;

	/**
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Temporary Contract the created object
	 * @throws TemporaryContractException When an error is thrown during the process
	 * @throws PdfGeneratorException
	 */
	TemporaryContract createTemporaryContractAmendment(CreateTemporaryAmendment contractPublic)
			throws TemporaryContractException, PdfGeneratorException;

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return The list of Temporary Contract amendment
	 */
	Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId);

}