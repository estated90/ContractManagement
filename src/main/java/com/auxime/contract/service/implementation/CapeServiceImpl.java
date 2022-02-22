package com.auxime.contract.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.cape.ContractCreate;
import com.auxime.contract.dto.cape.CapePublic;
import com.auxime.contract.dto.cape.ContractUpdate;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.repository.CapeRepository;

@Service
@Transactional
public class CapeServiceImpl {

	private static final Logger logger = LogManager.getLogger(CapeServiceImpl.class);
	private CapeRepository capeRepo;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of Cape
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Cape> getAllCape() {
		return capeRepo.findAll();
	}

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @return The list of Cape
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Cape> getAllCapeFromAccount(UUID accountId) {
		return capeRepo.findByAccountId(accountId);
	}

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param publicId The public ID is an UUID linked to the accounts of the users
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<Cape> getContractById(UUID id) {
		logger.info("Returning CAPE with id {}", id);
		Optional<Cape> capeOpt = capeRepo.findById(id);
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
	@Transactional(rollbackFor = { CapeException.class })
	public Cape createNewContract(ContractCreate contractPublic) throws CapeException {
		logger.info("Creating a new CAPE");
		
		Cape contract = settingCommonFields(new Cape(), contractPublic);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setContractType(ContractType.CONTRACT);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setStatus(true);
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
	@Transactional(rollbackFor = { CapeException.class })
	public Cape updateContractFromId(ContractUpdate contractPublic) throws CapeException {
		logger.info("Updating phone with id : {}", contractPublic.getContractId());
		Optional<Cape> contractOpt = capeRepo.findById(contractPublic.getContractId());
		if (contractOpt.isPresent()) {
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
	 * @throws CapeException     When an error is raised if not found
	 * @throws ActivityException
	 */
	@Transactional(rollbackFor = { CapeException.class })
	public void deleteContract(ContractUpdate contractPublic) throws CapeException {
		logger.info("Deleting a new activity fields {}", contractPublic.getContractId());
		Optional<Cape> contractOpt = capeRepo.findById(contractPublic.getContractId());
		if (contractOpt.isPresent()) {
			logger.info("Activity is in DB and is being deleted");
			capeRepo.delete(contractOpt.get());
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
		contract.setIdAccount(contractPublic.getIdAccount());
		contract.setEndDate(contractPublic.getStartingDate().plusYears(1));
		contract.setFse(contractPublic.isFse());
		return contract;
	}
}
