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
import com.auxime.contract.dto.cape.CapeCreate;
import com.auxime.contract.dto.cape.CapePublic;
import com.auxime.contract.dto.cape.CapeUpdate;
import com.auxime.contract.dto.cape.CreateCapeAmendment;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.repository.CapeRepository;
import com.auxime.contract.service.CapeService;

/**
 * @author Nicolas
 * @version 1.0.0
 *
 */
@Service
@Transactional
public class CapeServiceImpl implements CapeService {

	private static final Logger logger = LogManager.getLogger(CapeServiceImpl.class);
	@Autowired
	private CapeRepository capeRepo;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of Cape
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Cape> getAllCape() {
		return capeRepo.findAll();
	}

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return The list of Cape amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Cape> getAllAmendmentContract(UUID contractId) {
		return capeRepo.FindAllAmendment(contractId);
	}

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @param accountId ID of the account to extract the contract from
	 * @return The list of Cape
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Cape> getAllCapeFromAccount(UUID accountId) {
		return capeRepo.findByAccountId(accountId);
	}

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<Cape> getContractById(UUID contractId) {
		logger.info("Returning CAPE with id {}", contractId);
		Optional<Cape> capeOpt = capeRepo.findById(contractId);
		return capeOpt;
	}

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new updated contract object will be returned
	 * @throws CapeException When an error is detected
	 */
	@Override
	@Transactional(rollbackFor = { CapeException.class })
	public Cape createNewContract(CapeCreate contractPublic) throws CapeException {
		logger.info("Creating a new CAPE");
		Cape contract = settingCommonFields(new Cape(), contractPublic);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setContractType(ContractType.CONTRACT);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setStatus(true);
		contract.setAccountId(contractPublic.getAccountId());
		return capeRepo.save(contract);
	}

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws CapeException When an error is detected
	 */
	@Override
	@Transactional(rollbackFor = { CapeException.class })
	public Cape updateContractFromId(CapeUpdate contractPublic) throws CapeException {
		logger.info("Updating CAPE with id : {}", contractPublic.getContractId());
		Optional<Cape> contractOpt = capeRepo.findById(contractPublic.getContractId());
		if (contractOpt.isPresent() && contractOpt.get().isStatus()) {
			logger.info(ExceptionMessageConstant.CAPE_FOUND);
			Cape contract = settingCommonFields(contractOpt.get(), contractPublic);
			contract.setUpdatedAt(LocalDateTime.now());
			return capeRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.CAPE_NOT_FOUND);
			throw new CapeException(ExceptionMessageConstant.CAPE_NOT_FOUND);
		}
	}

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws CapeException When an error is raised if not found
	 */
	@Override
	@Transactional(rollbackFor = { CapeException.class })
	public void deleteContract(CapeUpdate contractPublic) throws CapeException {
		logger.info("Deleting a CAPE {}", contractPublic.getContractId());
		Cape cape = capeVerifier(contractPublic);
		cape.setStatus(false);
		capeRepo.save(cape);
	}

	private Cape capeVerifier(CapeUpdate contractPublic) throws CapeException {
		logger.info("Deleting a CAPE {}", contractPublic.getContractId());
		Optional<Cape> contractOpt = capeRepo.findById(contractPublic.getContractId());
		if (contractOpt.isPresent() && contractOpt.get().isStatus()) {
			return contractOpt.get();
		} else {
			logger.error(ExceptionMessageConstant.CAPE_NOT_FOUND);
			throw new CapeException(ExceptionMessageConstant.CAPE_NOT_FOUND);
		}
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
	private Cape settingCommonFields(Cape contract, CapePublic contractPublic) {
		logger.info("Updating the fields");
		contract.setContractDate(contractPublic.getContractDate());
		contract.setStartingDate(contractPublic.getStartingDate());
		contract.setContractTitle(contractPublic.getContractTitle());
		contract.setStructureContract(contractPublic.getStructureContract());
		contract.setEndDate(contractPublic.getStartingDate().plusYears(1));
		contract.setFse(contractPublic.getFse());
		contract.createStateContract();
		return contract;
	}

	/**
	 * Create an addendum to a CAPE contract
	 * 
	 * @param contract The object contract with the fields mandatory
	 * @return CAPE Contract the created object
	 * @throws CapeException When an error is thrown during the process
	 */
	@Override
	public Cape createAmendmentCape(CreateCapeAmendment contract) throws CapeException {
		logger.info("Creat an amendment to CAPE {}", contract.getContractAmendment());
		if (capeRepo.existsById(contract.getContractAmendment())) {
			Cape cape = settingCommonFields(new Cape(), contract);
			cape.createStateContract();
			cape.setAccountId(contract.getAccountId());
			cape.setStatus(true);
			cape.setContractType(ContractType.AMENDMENT);
			cape.setContractAmendment(contract.getContractAmendment());
			cape.setCreatedAt(LocalDateTime.now());
			return capeRepo.save(cape);
		} else {
			logger.error(ExceptionMessageConstant.CAPE_NOT_FOUND);
			throw new CapeException(ExceptionMessageConstant.CAPE_NOT_FOUND);
		}
	}
}
