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
import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialPublic;
import com.auxime.contract.dto.commercial.CommercialUpdate;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.exception.CommercialContractException;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.repository.CommercialRepository;
import com.auxime.contract.service.CommercialContractService;

@Service
@Transactional
public class CommercialContractServiceImpl implements CommercialContractService {

	private static final Logger logger = LogManager.getLogger(CommercialContractServiceImpl.class);
	@Autowired
	private CommercialRepository commercialeRepo;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of Cape
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CommercialContract> getAllCommercial() {
		return commercialeRepo.findAll();
	}

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @return The list of Cape
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CommercialContract> getAllCommercialFromAccount(UUID accountId) {
		return commercialeRepo.findByAccountId(accountId);
	}

	/**
	 * This function is using the ID of a cape to return its informations
	 * 
	 * @param publicId The public ID is an UUID linked to the accounts of the users
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<CommercialContract> getCommercialById(UUID id) {
		logger.info("Returning Commercial Contract with id {}", id);
		return commercialeRepo.findById(id);
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
	@Transactional(rollbackFor = { CommercialContractException.class })
	public CommercialContract createNewCommercial(CommercialCreate contractPublic) throws CommercialContractException {
		logger.info("Creating a new Commercial Contract");
		CommercialContract contract = settingCommonFields(new CommercialContract(), contractPublic);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setContractType(ContractType.CONTRACT);
		contract.setCreatedAt(LocalDateTime.now());
		contract.setStatus(true);
		contract.setAccountId(contractPublic.getAccountId());
		return commercialeRepo.save(contract);
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
	@Transactional(rollbackFor = { CommercialContractException.class })
	public CommercialContract updateCommercialFromId(CommercialUpdate contractPublic) throws CommercialContractException {
		logger.info("Updating Commercial Contract with id : {}", contractPublic.getContractId());
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		logger.info(ExceptionMessageConstant.CAPE_FOUND);
		CommercialContract contract = settingCommonFields(contractOpt.get(), contractPublic);
		contract.setUpdatedAt(LocalDateTime.now());
		return commercialeRepo.save(contract);
	}

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws CapeException     When an error is raised if not found
	 * @throws ActivityException
	 */
	@Override
	@Transactional(rollbackFor = { CommercialContractException.class })
	public void deleteCommercial(CommercialUpdate contractPublic) throws CommercialContractException {
		logger.info("Deleting Commercial Contract {}", contractPublic.getContractId());
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		logger.info("Activity is in DB and is being deleted");
		contractOpt.get().setStatus(false);
		commercialeRepo.save(contractOpt.get());
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
	private CommercialContract settingCommonFields(CommercialContract contract, CommercialPublic contractPublic) {
		logger.info("Updating the fields");
		contract.setContractDate(contractPublic.getContractDate());
		contract.setStartingDate(contractPublic.getStartingDate());
		contract.setContractTitle(contractPublic.getContractTitle());
		contract.setStructureContract(contractPublic.getStructureContract());
		contract.setEndDate(contractPublic.getEndDate());
		contract.setClientId(contractPublic.getClientId());
		contract.setGlobalAmount(Math.round(contractPublic.getGlobalAmount()*100.0)/100.0);
		contract.setMonthlyAmount(Math.round(contractPublic.getMonthlyAmount()*100.0)/100.0);
		contract.setMissionDuration(contractPublic.getMissionDuration());
		contract.setDurationUnit(contractPublic.getDurationUnit());
		return contract;
	}
}
