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
import com.auxime.contract.dto.permanent.CreatePermanentAmendment;
import com.auxime.contract.dto.permanent.PermanentCreate;
import com.auxime.contract.dto.permanent.PermanentUpdate;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.exception.PermanentContractException;
import com.auxime.contract.model.PermanentContract;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.proxy.AccountFeign;
import com.auxime.contract.repository.PermanentContractRepository;
import com.auxime.contract.service.PermanentContractService;
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
public class PermanentContractServiceImpl implements PermanentContractService {

	private static final Logger logger = LogManager.getLogger(PermanentContractServiceImpl.class);
	@Autowired
	private PermanentContractRepository permanentRepo;
	@Autowired
	private ContractsSpecification builder;
	@Autowired
	private AccountFeign proxy;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of PermanentContract
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllPermanentContract(int page, int size, String filter, LocalDate startDate,
			LocalDate endDate,
			ContractState contractState, PortageCompanies structureContract) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<PermanentContract> pagedResult = permanentRepo.findAll(
				builder.filterSqlPermanent(filter, startDate, endDate, contractState, structureContract), paging);
		return createPagination(pagedResult);
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
		Page<PermanentContract> pagedResult = permanentRepo.findAllAmendment(contractId, paging);
		return createPagination(pagedResult);
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
		return createPagination(pagedResult);
	}

	private Map<String, Object> createPagination(Page<PermanentContract> pagedResult) {
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
	 * @throws PdfGeneratorException
	 */
	@Override
	@Transactional(rollbackFor = { PermanentContractException.class })
	public PermanentContract createNewContract(PermanentCreate contractPublic)
			throws PermanentContractException, PdfGeneratorException {
		logger.info("Creating a new Permanent Contract");
		if (!proxy.getAccountsyExist(contractPublic.getAccountId())) {
			logger.error(ExceptionMessageConstant.ACCOUNT_NOT_FOUND);
			throw new PermanentContractException(ExceptionMessageConstant.ACCOUNT_NOT_FOUND);
		}
		PermanentContract contract = new PermanentContract().buildPermanentContract(contractPublic);
		writtingFileAsPdf(contract, ContractsName.PERMANENT_CONTRACT_COELIS.getFileName());
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
		PermanentContract contract = contractOpt.get().buildPermanentCommon(contractPublic);
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
	 * Create an addendum to a temporary contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return Portage Convention the created object
	 * @throws PermanentContractException When an error is thrown during the process
	 * @throws PdfGeneratorException
	 */
	@Override
	public PermanentContract createPermanentContractAmendment(CreatePermanentAmendment contractPublic)
			throws PermanentContractException, PdfGeneratorException {
		logger.info("Creat an amendment to CAPE {}", contractPublic.getContractAmendment());
		if (permanentRepo.existsById(contractPublic.getContractAmendment())) {
			PermanentContract contract = new PermanentContract().buildPermanentContractAmend(contractPublic);
			writtingFileAsPdf(contract, ContractsName.PERMANENT_CONTRACT_COELIS.getFileName());
			return permanentRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
			throw new PermanentContractException(ExceptionMessageConstant.PERMANENT_CONTRACT_NOT_FOUND);
		}
	}

	@Autowired
	private AccountFeign accountFeign;
	@Autowired
	private PdfGenerator pdfGenerator;

	private void writtingFileAsPdf(PermanentContract contract, String file) throws PdfGeneratorException {
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
