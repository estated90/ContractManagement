package com.auxime.contract.service.implementation;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.portage.CreatePortageAmendment;
import com.auxime.contract.dto.portage.PortageCreate;
import com.auxime.contract.dto.portage.PortagePublic;
import com.auxime.contract.dto.portage.PortageUpdate;
import com.auxime.contract.exception.PortageConventionException;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.repository.PortageConventionRepository;
import com.auxime.contract.service.PortageConventionService;

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
public class PortageConventionServiceImpl implements PortageConventionService {

	private static final Logger logger = LogManager.getLogger(PortageConventionServiceImpl.class);
	@Autowired
	private PortageConventionRepository portageRepo;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of PortageConvention
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllContract(int page, int size) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PortageConvention> pagedResult = portageRepo.findAll(paging);
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
	 * @return The list of Portage Convention amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PortageConvention> pagedResult = portageRepo.findAllAmendment(contractId, paging);
		Map<String, Object> response = new HashMap<>();
		response.put("contracts", pagedResult.toList());
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());
		return response;
	}

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @return The list of Cape
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllContractFromAccount(int page, int size, UUID accountId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PortageConvention> pagedResult = portageRepo.findByAccountId(accountId, paging);
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
	public Optional<PortageConvention> getContractById(UUID contractId) {
		logger.info("Returning Portage Convention with id {}", contractId);
		return portageRepo.findById(contractId);
	}

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new created contract object will be returned
	 * @throws PortageConventionException When an error is detected
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
	 * @throws PortageConventionException When an error is detected
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
	 * @throws PortageConventionException When an error is raised if not found
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

	/**
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Portage Convention the created object
	 * @throws PortageConventionException When an error is thrown during the process
	 */
	@Override
	public PortageConvention createPortageConventionContract(CreatePortageAmendment contractPublic)
			throws PortageConventionException {
		logger.info("Creat an amendment to CAPE {}", contractPublic.getContractAmendment());
		if (portageRepo.existsById(contractPublic.getContractAmendment())) {
			PortageConvention contract = settingCommonFields(new PortageConvention(), contractPublic);
			contract.createStateContract();
			contract.setAccountId(contractPublic.getAccountId());
			contract.setStatus(true);
			contract.setContractType(ContractType.AMENDMENT);
			contract.setContractAmendment(contractPublic.getContractAmendment());
			contract.setCreatedAt(LocalDateTime.now());
			return portageRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
			throw new PortageConventionException(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
		}
	}
}
