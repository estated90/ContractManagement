package com.auxime.contract.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.model.Contract;
import com.auxime.contract.model.enums.PortageCompanies;

/**
 * @author Nicolas
 * @version 1.0.0
 */
public interface ContractService {

	/**
	 * Return all contract without a filter on the contract type
	 * 
	 * @param page              Page number to display
	 * @param size              Size of the page
	 * @param filter            Filter on the contract name
	 * @param startDate         Filter as range for the contract start date
	 * @param endDate           Filter as range for the contract end date
	 * @param contractState     State of the contract
	 * @param structureContract Company that legaly sign the contract
	 * @return Map<String, Object> Return object with the information of the page
	 *         and list of the result
	 */
	Map<String, Object> getAllContracts(int page, int size, String filter, LocalDate startDate, LocalDate endDate,
			ContractState contractState, PortageCompanies structureContract);

	/**
	 * Method to get a contract by ID
	 * 
	 * @param contractId UUID of the contract
	 * @return Optional<Contract> The founded contract in DB
	 */
	Optional<Contract> getContractById(UUID contractId);

}