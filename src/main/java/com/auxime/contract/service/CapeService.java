package com.auxime.contract.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.dto.cape.CapeCreate;
import com.auxime.contract.dto.cape.CapeUpdate;
import com.auxime.contract.dto.cape.CreateCapeAmendment;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.enums.PortageCompanies;

/**
 * @author Nicolas
 * @version 1.0.0
 */
public interface CapeService {

	/**
	 * Method to return all contract in DB
	 * 
	 * @param rate
	 * @param structureContract
	 * @param contractState
	 * @param dates
	 * @param filter
	 * 
	 * @return The list of Cape
	 */
	Map<String, Object> getAllCape(int page, int size, String filter, Map<String, LocalDate> dates,
			ContractState contractState, PortageCompanies structureContract, Integer rate);

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @param accountId ID of the account to extract the contract from
	 * @return The list of Cape
	 * 
	 */
	Map<String, Object> getAllCapeFromAccount(int page, int size, UUID accountId);

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	Optional<Cape> getContractById(UUID contractId);

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new updated contract object will be returned
	 * @throws CapeException When an error is detected
	 * @throws Exception
	 */
	Cape createNewContract(CapeCreate contractPublic) throws PdfGeneratorException;

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws CapeException When an error is detected
	 */
	Cape updateContractFromId(CapeUpdate contractPublic) throws CapeException;

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws CapeException When an error is raised if not found
	 */
	void deleteContract(UUID contractId) throws CapeException;

	/**
	 * Create an addendum to a CAPE contract
	 * 
	 * @param contract The object contract with the fields mandatory
	 * @return CAPE Contract the created object
	 * @throws CapeException When an error is thrown during the process
	 */
	Cape createAmendmentCape(CreateCapeAmendment contract) throws CapeException;

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return The list of Cape amendment
	 */
	Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId);

}