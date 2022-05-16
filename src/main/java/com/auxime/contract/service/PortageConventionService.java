package com.auxime.contract.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.dto.portage.CreatePortageAmendment;
import com.auxime.contract.dto.portage.PortageCreate;
import com.auxime.contract.dto.portage.PortageUpdate;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.exception.PortageConventionException;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.enums.PortageCompanies;

/**
 * @author Nicolas
 *
 */
public interface PortageConventionService {

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of PortageConvention
	 */
	Map<String, Object> getAllContract(int page, int size, String filter, LocalDate startDate, LocalDate endDate,
	ContractState contractState, PortageCompanies structureContract);

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param accountId The the contract ID to look the amendment linked to.
	 * @return The list of Commercial Contract amendment
	 */
	Map<String, Object> getAllContractFromAccount(int page, int size, UUID accountId);

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	Optional<PortageConvention> getContractById(UUID contractId);

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new created contract object will be returned
	 * @throws PortageConventionException When an error is detected
	 * @throws PdfGeneratorException
	 */
	PortageConvention createNewContract(PortageCreate contractPublic) throws PortageConventionException, PdfGeneratorException;

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws PortageConventionException When an error is detected
	 */
	PortageConvention updateContractFromId(PortageUpdate contractPublic) throws PortageConventionException;

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws PortageConventionException When an error is raised if not found
	 */
	void deleteContract(UUID contractId) throws PortageConventionException;

	/**
	 * Create an addendum to a Portage Convention
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Portage Convention the created object
	 * @throws PortageConventionException When an error is thrown during the process
	 * @throws PdfGeneratorException
	 */
	PortageConvention createPortageConventionContract(CreatePortageAmendment contractPublic)
			throws PortageConventionException, PdfGeneratorException;

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return The list of Portage Convention amendment
	 */
	Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId);

}