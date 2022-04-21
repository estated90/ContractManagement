package com.auxime.contract.service.implementation;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.permanent.CreatePermanentAmendment;
import com.auxime.contract.dto.permanent.PermanentCreate;
import com.auxime.contract.dto.permanent.PermanentPublic;
import com.auxime.contract.dto.permanent.PermanentUpdate;
import com.auxime.contract.exception.PermanentContractException;
import com.auxime.contract.model.PermanentContract;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.repository.PermanentContractRepository;
import com.auxime.contract.service.PermanentContractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nicolas
 *
 */
@Service
@Transactional
public class PermanentContractServiceImpl implements PermanentContractService {

	private static final Logger logger = LogManager.getLogger(PermanentContractServiceImpl.class);
	@Autowired
	private PermanentContractRepository permanentRepo;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of PermanentContract
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllPermanentContract(int page, int size) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PermanentContract> pagedResult = permanentRepo.findAll(paging);
		Map<String, Object> response = new HashMap<>();
		response.put("contracts", pagedResult.toList());
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());
		return response;
	}

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return The list of Permanent Contract amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PermanentContract> pagedResult = permanentRepo.FindAllAmendment(contractId, paging);
		Map<String, Object> response = new HashMap<>();
		response.put("contracts", pagedResult.toList());
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());
		return response;
	}
	
	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param accountId The the contract ID to look the amendment linked to.
	 * @return The list of Permanent Contract amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllPermanentContractFromAccount(int page, int size, UUID accountId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PermanentContract> pagedResult = permanentRepo.findByAccountId(accountId, paging);
		Map<String, Object> response = new HashMap<>();
		response.put("contracts", pagedResult.toList());
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());
		return response;
	}

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<PermanentContract> getContractById(UUID contractId) {
		logger.info("Returning Permanent Contract with id {}", contractId);
		return permanentRepo.findById(contractId);
	}

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new created contract object will be returned
	 * @throws PermanentContractException When an error is detected
	 */
	@Override
	@Transactional(rollbackFor = { PermanentContractException.class })
	public PermanentContract createNewContract(PermanentCreate contractPublic) throws PermanentContractException {
		logger.info("Creating a new Permanent Contract");
		PermanentContract contract = settingCommonFields(new PermanentContract(), contractPublic);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setContractType(ContractType.CONTRACT);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setStatus(true);
		contract.setAccountId(contractPublic.getAccountId());
		contract.createStateContract();
		return permanentRepo.save(contract);
	}

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws PermanentContractException When an error is detected
	 */
	@Override
	@Transactional(rollbackFor = { PermanentContractException.class })
	public PermanentContract updateContractFromId(PermanentUpdate contractPublic) throws PermanentContractException {
		logger.info("Updating Permanent Contract with id : {}", contractPublic.getContractId());
		Optional<PermanentContract> contractOpt = permanentRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
			throw new PermanentContractException(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
		}
		PermanentContract contract = settingCommonFields(contractOpt.get(), contractPublic);
		contract.setUpdatedAt(LocalDateTime.now());
		return permanentRepo.save(contract);
	}

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws PermanentContractException When an error is raised if not found
	 */
	@Override
	@Transactional(rollbackFor = { PermanentContractException.class })
	public void deleteContract(UUID contractId) throws PermanentContractException {
		logger.info("Deleting a Permanent Contract {}", contractId);
		Optional<PermanentContract> contractOpt = permanentRepo.findById(contractId);
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
			throw new PermanentContractException(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
		}
		logger.info("Activity is in DB and is being deleted");
		contractOpt.get().setStatus(false);
		permanentRepo.save(contractOpt.get());
	}

	/**
	 * 
	 * Function to update the object for the common fields between creation and
	 * update
	 * 
	 * @param contract       The contract found in the DB
	 * @param contractPublic The object with the new information to use for the
	 *                       update
	 * @return The contract updated
	 */
	private PermanentContract settingCommonFields(PermanentContract contract, PermanentPublic contractPublic) {
		logger.info("Updating the fields");
		contract.setContractDate(contractPublic.getContractDate());
		contract.setStartingDate(contractPublic.getStartingDate());
		contract.setContractTitle(contractPublic.getContractTitle());
		contract.setStructureContract(contractPublic.getStructureContract());
		contract.setEndDate(contractPublic.getStartingDate().plusYears(1));
		contract.setHourlyRate(contractPublic.getHourlyRate());
		contract.setRuptureDate(contractPublic.getRuptureDate());
		contract.setWorkTime(contractPublic.getWorkTime());
		return contract;
	}

	/**
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Portage Convention the created object
	 * @throws PermanentContractException When an error is thrown during the process
	 */
	@Override
	public PermanentContract createPermanentContractAmendment(CreatePermanentAmendment contractPublic)
			throws PermanentContractException {
		logger.info("Creat an amendment to CAPE {}", contractPublic.getContractAmendment());
		if (permanentRepo.existsById(contractPublic.getContractAmendment())) {
			PermanentContract contract = settingCommonFields(new PermanentContract(), contractPublic);
			contract.createStateContract();
			contract.setAccountId(contractPublic.getAccountId());
			contract.setStatus(true);
			contract.setContractType(ContractType.AMENDMENT);
			contract.setContractAmendment(contractPublic.getContractAmendment());
			contract.setCreatedAt(LocalDateTime.now());
			return permanentRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
			throw new PermanentContractException(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
		}
	}
}
