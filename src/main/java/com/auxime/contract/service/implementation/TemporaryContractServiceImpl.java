package com.auxime.contract.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.auxime.contract.builder.ContractsSpecification;
import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractsName;
import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.temporary.CreateTemporaryAmendment;
import com.auxime.contract.dto.temporary.TemporaryCreate;
import com.auxime.contract.dto.temporary.TemporaryUpdate;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.exception.TemporaryContractException;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.TemporaryContract;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.proxy.AccountFeign;
import com.auxime.contract.repository.TemporaryContractRepository;
import com.auxime.contract.service.TemporaryContractService;
import com.auxime.contract.utils.GenerateListVariable;
import com.auxime.contract.utils.PdfGenerator;

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
	@Autowired
	private ContractsSpecification builder;
	@Autowired
	private AccountFeign proxy;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of TemporaryContract
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllContract(int page, int size, String filter, LocalDate startDate, LocalDate endDate,
			ContractState contractState, PortageCompanies structureContract) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<TemporaryContract> pagedResult = temporaryRepo.findAll(
				builder.filterSqlTemporary(filter, startDate, endDate, contractState, structureContract), paging);
		return createPagination(pagedResult);
	}

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId the ID of the contract to extract the details from.
	 * @return The list of Temporary Contract amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<TemporaryContract> pagedResult = temporaryRepo.findAllAmendment(contractId, paging);
		return createPagination(pagedResult);
	}

	/**
	 * Method to return all contract in DB from account
	 * 
	 * @param accountId The the contract ID to look the amendment linked to.
	 * @return The list of Temporary Contract amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllContractFromAccount(int page, int size, UUID accountId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<TemporaryContract> pagedResult = temporaryRepo.findByAccountId(accountId, paging);
		return createPagination(pagedResult);
	}

	private Map<String, Object> createPagination(Page<TemporaryContract> pagedResult) {
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
	 * @throws PdfGeneratorException
	 */
	@Override
	@Transactional(rollbackFor = { TemporaryContractException.class })
	public TemporaryContract createNewContract(TemporaryCreate contractPublic)
			throws TemporaryContractException, PdfGeneratorException {
		logger.info("Creating a new Temporary Contract");
		if (!proxy.getAccountsyExist(contractPublic.getAccountId())) {
			logger.error(ExceptionMessageConstant.ACCOUNT_NOT_FOUND);
			throw new TemporaryContractException(ExceptionMessageConstant.ACCOUNT_NOT_FOUND);
		}
		TemporaryContract contract = new TemporaryContract().buildTemporary(contractPublic);
		writtingFileAsPdf(contract, ContractsName.TEMPORARY_CONTRACT_COELIS.getFileName());
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
		TemporaryContract contract = contractOpt.get().buildTemporaryCommon(contractPublic);
		contract.setUpdatedAt(LocalDateTime.now());
		return temporaryRepo.save(contract);
	}

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractId The object activityPublic with the fields mandatory
	 * @throws TemporaryContractException When an error is raised if not found
	 */
	@Override
	@Transactional(rollbackFor = { TemporaryContractException.class })
	public void deleteContract(UUID contractId) throws TemporaryContractException {
		logger.info("Deleting a Temporary Contract {}", contractId);
		TemporaryContract contract = contractVerifier(contractId);
		contract.setStatus(false);
		temporaryRepo.save(contract);
	}
	
	private TemporaryContract contractVerifier(UUID contractId) throws TemporaryContractException {
		logger.info("Deleting a CAPE {}", contractId);
		Optional<TemporaryContract> contractOpt = temporaryRepo.findById(contractId);
		if (contractOpt.isPresent() && contractOpt.get().isStatus()) {
			return contractOpt.get();
		} else {
			logger.error(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
			throw new TemporaryContractException(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
		}
	}

	/**
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Temporary Contract the created object
	 * @throws TemporaryContractException When an error is thrown during the process
	 * @throws PdfGeneratorException
	 */
	@Override
	public TemporaryContract createTemporaryContractAmendment(CreateTemporaryAmendment contractPublic)
			throws TemporaryContractException, PdfGeneratorException {
		logger.info("Creat an amendment to CAPE {}", contractPublic.getContractAmendment());
		if (temporaryRepo.existsById(contractPublic.getContractAmendment())) {
			TemporaryContract contract = new TemporaryContract().buildTemporary(contractPublic);
			writtingFileAsPdf(contract, ContractsName.TEMPORARY_CONTRACT_COELIS.getFileName());
			return temporaryRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
			throw new TemporaryContractException(ExceptionMessageConstant.TEMPORARY_CONTRACT_NOT_FOUND);
		}
	}

	@Autowired
	private AccountFeign accountFeign;
	@Autowired
	private PdfGenerator pdfGenerator;

	private void writtingFileAsPdf(TemporaryContract contract, String file) throws PdfGeneratorException {
		// Getting the info linked to the profile of contract account
		Optional<ProfileInfo> profileInfo = accountFeign.getProfilesFromAccountId(contract.getAccountId());
		if (profileInfo.isEmpty()) {
			logger.error(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
			throw new PdfGeneratorException(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
		}
		Map<String, String> listWords = GenerateListVariable.setListVariable(contract, profileInfo.get());
		String fileName = contract.getContractType().toString() + " COMMERCIAL CONTRACT "
				+ profileInfo.get().getLastName() + " "
				+ profileInfo.get().getFistName() + " "
				+ LocalDateTime.now().toString().replace("-", "_").replace(":", "_");
		pdfGenerator.replaceTextModel(listWords, fileName, file);
	}
}
