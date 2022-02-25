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
import com.auxime.contract.dto.portage.PortageCreate;
import com.auxime.contract.dto.portage.PortagePublic;
import com.auxime.contract.dto.portage.PortageUpdate;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.exception.PortageConventionException;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.repository.PortageConventionRepository;
import com.auxime.contract.service.PortageConventionService;

@Service
@Transactional
public class PortageConventionServiceImpl implements PortageConventionService {

	private static final Logger logger = LogManager.getLogger(PortageConventionServiceImpl.class);
	@Autowired
	private PortageConventionRepository portageRepo;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of Cape
	 */ 
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PortageConvention> getAllContract() {
		return portageRepo.findAll();
	}

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @return The list of Cape
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<PortageConvention> getAllContractFromAccount(UUID accountId) {
		return portageRepo.findByAccountId(accountId);
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
	public Optional<PortageConvention> getContractById(UUID id) {
		logger.info("Returning Portage Convention with id {}", id);
		return portageRepo.findById(id);
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
	@Transactional(rollbackFor = { PortageConventionException.class })
	public PortageConvention createNewContract(PortageCreate contractPublic) throws PortageConventionException {
		logger.info("Creating a new Portage Convention");
		PortageConvention portage = settingCommonFields(new PortageConvention(), contractPublic);
		portage.setCreatedAt(LocalDateTime.now());
		portage.setContractType(ContractType.CONTRACT);
		portage.setStatus(true);
		portage.setAccountId(contractPublic.getAccountId());
		portage.setEndDate(contractPublic.getEndDate());
		portage.createStateContract();
		return portageRepo.save(portage);
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
	@Transactional(rollbackFor = { PortageConventionException.class })
	public PortageConvention updateContractFromId(PortageUpdate contractPublic) throws PortageConventionException {
		logger.info("Updating Portage Convention with id : {}", contractPublic.getContractId());
		Optional<PortageConvention> contractOpt = portageRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
			throw new PortageConventionException(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
		}
		logger.info(ExceptionMessageConstant.CAPE_FOUND);
		PortageConvention portage = settingCommonFields(contractOpt.get(), contractPublic);
		portage.setUpdatedAt(LocalDateTime.now());
		return portageRepo.save(portage);
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
	@Transactional(rollbackFor = { PortageConventionException.class })
	public void deleteContract(PortageUpdate contractPublic) throws PortageConventionException {
		logger.info("Deleting a Portage Convention {}", contractPublic.getContractId());
		Optional<PortageConvention> contractOpt = portageRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
			throw new PortageConventionException(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
		}
		logger.info("Activity is in DB and is being deleted");
		contractOpt.get().setStatus(false);
		portageRepo.save(contractOpt.get());
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
	private PortageConvention settingCommonFields(PortageConvention contract, PortagePublic contractPublic) {
		logger.info("Updating the fields");
		contract.setContractDate(contractPublic.getContractDate());
		contract.setStartingDate(contractPublic.getStartingDate());
		contract.setContractTitle(contractPublic.getContractTitle());
		contract.setStructureContract(contractPublic.getStructureContract());
		return contract;
	}
}
