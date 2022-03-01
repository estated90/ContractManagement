package com.auxime.contract.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.temporary.CreateTemporaryAmendment;
import com.auxime.contract.dto.temporary.TemporaryCreate;
import com.auxime.contract.dto.temporary.TemporaryPublic;
import com.auxime.contract.dto.temporary.TemporaryUpdate;
import com.auxime.contract.exception.TemporaryContractException;
import com.auxime.contract.model.TemporaryContract;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.repository.TemporaryContractRepository;
import com.auxime.contract.service.TemporaryContractService;

/**
 * @author Nicolas
 *
 */
@Service
@Transactional
public class TemporaryContractServiceImpl implements TemporaryContractService {

	private static final Logger logger = LogManager.getLogger(TemporaryContractServiceImpl.class);

	@Autowired
	private TemporaryContractRepository temporaryRepo;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of TemporaryContract
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TemporaryContract> getAllContract() {
		return temporaryRepo.findAll();
	}

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return The list of Temporary Contract amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TemporaryContract> getAllAmendmentContract(UUID contractId) {
		return temporaryRepo.FindAllAmendment(contractId);
	}
	
	/**
	 * Method to return all contract in DB from account
	 * 
	 * @param accountId The the contract ID to look the amendment linked to.
	 * @return The list of Temporary Contract amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<TemporaryContract> getAllContractFromAccount(UUID accountId) {
		return temporaryRepo.findByAccountId(accountId);
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
	public Optional<TemporaryContract> getContractById(UUID contractId) {
		logger.info("Returning Temporary Contract with id {}", contractId);
		return temporaryRepo.findById(contractId);
	}

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new created contract object will be returned
	 * @throws TemporaryContractException When an error is detected
	 */
	@Override
	@Transactional(rollbackFor = { TemporaryContractException.class })
	public TemporaryContract createNewContract(TemporaryCreate contractPublic) throws TemporaryContractException {
		logger.info("Creating a new Temporary Contract");
		TemporaryContract contract = settingCommonFields(new TemporaryContract(), contractPublic);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setContractType(ContractType.CONTRACT);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setStatus(true);
		contract.setAccountId(contractPublic.getAccountId());
		contract.createStateContract();
		return temporaryRepo.save(contract);
	}

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws TemporaryContractException When an error is detected
	 */
	@Override
	@Transactional(rollbackFor = { TemporaryContractException.class })
	public TemporaryContract updateContractFromId(TemporaryUpdate contractPublic) throws TemporaryContractException {
		logger.info("Updating Temporary Contract with id : {}", contractPublic.getContractId());
		Optional<TemporaryContract> contractOpt = temporaryRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
			throw new TemporaryContractException(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
		}
		TemporaryContract contract = settingCommonFields(contractOpt.get(), contractPublic);
		contract.setUpdatedAt(LocalDateTime.now());
		return temporaryRepo.save(contract);
	}

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws TemporaryContractException When an error is raised if not found
	 */
	@Override
	@Transactional(rollbackFor = { TemporaryContractException.class })
	public void deleteContract(TemporaryUpdate contractPublic) throws TemporaryContractException {
		logger.info("Deleting a Temporary Contract {}", contractPublic.getContractId());
		Optional<TemporaryContract> contractOpt = temporaryRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
			throw new TemporaryContractException(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
		}
		logger.info("Activity is in DB and is being deleted");
		contractOpt.get().setStatus(false);
		temporaryRepo.save(contractOpt.get());
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
	private TemporaryContract settingCommonFields(TemporaryContract contract, TemporaryPublic contractPublic) {
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
	 * @return Temporary Contract the created object
	 * @throws TemporaryContractException When an error is thrown during the process
	 */
	@Override
	public TemporaryContract createTemporaryContractAmendment(CreateTemporaryAmendment contractPublic)
			throws TemporaryContractException {
		logger.info("Creat an amendment to CAPE {}", contractPublic.getContractAmendment());
		if (temporaryRepo.existsById(contractPublic.getContractAmendment())) {
			TemporaryContract contract = settingCommonFields(new TemporaryContract(), contractPublic);
			contract.createStateContract();
			contract.setAccountId(contractPublic.getAccountId());
			contract.setStatus(true);
			contract.setContractType(ContractType.AMENDMENT);
			contract.setContractAmendment(contractPublic.getContractAmendment());
			contract.setCreatedAt(LocalDateTime.now());
			return temporaryRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
			throw new TemporaryContractException(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
		}
	}
}
