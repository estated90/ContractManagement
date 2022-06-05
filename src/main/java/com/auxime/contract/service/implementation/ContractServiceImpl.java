package com.auxime.contract.service.implementation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.Min;

import com.auxime.contract.builder.ContractsSpecification;
import com.auxime.contract.constants.ContractState;
import com.auxime.contract.model.Contract;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.repository.ContractRepository;
import com.auxime.contract.service.ContractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractRepository contractRepo;
	@Autowired
	private ContractsSpecification builder;

	private static final Logger logger = LogManager.getLogger(ContractServiceImpl.class);

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
	public Map<String, Object> getAllContracts(@Min(1) int page, @Min(1) int size, String filter, LocalDate startDate,
			LocalDate endDate,
			ContractState contractState, PortageCompanies structureContract) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<Contract> pagedResult = contractRepo.findAll(
				builder.filterSqlContracts(filter, startDate, endDate, contractState, structureContract), paging);
		return createPagination(pagedResult);
	}

	/**
	 * Function to create the object to return
	 * 
	 * @param pagedResult Result as page
	 * @return Map<String, Object> Return object with the information of the page
	 *         and list of the result
	 */
	private Map<String, Object> createPagination(Page<Contract> pagedResult) {
		Map<String, Object> response = new HashMap<>();
		response.put("contracts", pagedResult.toList());
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());
		return response;
	}

	/**
	 * Method to get a contract by ID
	 * 
	 * @param contractId UUID of the contract
	 * @return Optional<Contract> The founded contract in DB
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<Contract> getContractById(UUID contractId) {
		logger.info("Returning CAPE with id {}", contractId);
		return contractRepo.findById(contractId);
	}

}
