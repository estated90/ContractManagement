package com.auxime.contract.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.builder.ContractsSpecification;
import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractsName;
import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.portage.CreatePortageAmendment;
import com.auxime.contract.dto.portage.PortageCreate;
import com.auxime.contract.dto.portage.PortageUpdate;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.exception.PortageConventionException;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.proxy.AccountFeign;
import com.auxime.contract.repository.PortageConventionRepository;
import com.auxime.contract.service.PortageConventionService;
import com.auxime.contract.utils.GenerateListVariable;
import com.auxime.contract.utils.PdfGenerator;

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
	@Autowired
	private ContractsSpecification builder;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of PortageConvention
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllContract(int page, int size, String filter, LocalDate startDate, LocalDate endDate,
			ContractState contractState, PortageCompanies structureContract) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PortageConvention> pagedResult = portageRepo.findAll(
				builder.filterSqlPortage(filter, startDate, endDate, contractState, structureContract), paging);
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
	 * @throws PdfGeneratorException
	 */
	@Override
	@Transactional(rollbackFor = { PortageConventionException.class })
	public PortageConvention createNewContract(PortageCreate contractPublic) throws PortageConventionException, PdfGeneratorException {
		logger.info("Creating a new Portage Convention");
		PortageConvention portage = new PortageConvention().buildConvention(contractPublic);
		writtingFileAsPdf(portage, ContractsName.PORTAGE_CONVENTION_COELIS.getFileName());
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
		PortageConvention portage = contractOpt.get().buildConventionCommon(contractPublic);
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
	public void deleteContract(UUID contractId) throws PortageConventionException {
		logger.info("Deleting a Portage Convention {}", contractId);
		Optional<PortageConvention> contractOpt = portageRepo.findById(contractId);
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
			throw new PortageConventionException(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
		}
		logger.info("Activity is in DB and is being deleted");
		contractOpt.get().setStatus(false);
		portageRepo.save(contractOpt.get());
	}

	/**
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Portage Convention the created object
	 * @throws PortageConventionException When an error is thrown during the process
	 * @throws PdfGeneratorException
	 */
	@Override
	public PortageConvention createPortageConventionContract(CreatePortageAmendment contractPublic)
			throws PortageConventionException, PdfGeneratorException {
		logger.info("Creat an amendment to CAPE {}", contractPublic.getContractAmendment());
		if (portageRepo.existsById(contractPublic.getContractAmendment())) {
			PortageConvention contract = new PortageConvention().buildConvention(contractPublic);
			writtingFileAsPdf(contract, ContractsName.PORTAGE_CONVENTION_COELIS.getFileName());
			return portageRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
			throw new PortageConventionException(ExceptionMessageConstant.PORTAGE_CONVENTION_NOT_FOUND);
		}
	}

	@Autowired
	private AccountFeign accountFeign;
	@Autowired
	private PdfGenerator pdfGenerator;

	private void writtingFileAsPdf(PortageConvention contract, String file) throws PdfGeneratorException {
		// Getting the info linked to the profile of contract account
		Optional<ProfileInfo> profileInfo = accountFeign.getProfilesFromAccountId(contract.getAccountId());
		if (profileInfo.isEmpty()) {
			logger.error(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
			throw new PdfGeneratorException(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
		}
		Map<String, String> listWords = GenerateListVariable.setListVariable(contract, profileInfo.get());
		String fileName = contract.getContractType().toString() + " COMMERCIAL CONTRACT " + profileInfo.get().getLastName() + " "
				+ profileInfo.get().getFistName() + " "
				+ LocalDateTime.now().toString().replace("-", "_").replace(":", "_");
		pdfGenerator.replaceTextModel(listWords, fileName, file);
	}
}
