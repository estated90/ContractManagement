package com.auxime.contract.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.builder.ContractsSpecification;
import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.constants.ContractsName;
import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.CommentCommercialPublic;
import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialUpdate;
import com.auxime.contract.dto.commercial.CreateCommercialAmendment;
import com.auxime.contract.exception.CommercialContractException;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.model.CommentCommercialContract;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.proxy.AccountFeign;
import com.auxime.contract.repository.CommercialRepository;
import com.auxime.contract.service.CommercialContractService;
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
public class CommercialContractServiceImpl implements CommercialContractService {

	private static final Logger logger = LogManager.getLogger(CommercialContractServiceImpl.class);
	@Autowired
	private CommercialRepository commercialeRepo;
	@Autowired
	private ContractsSpecification builder;
	@Autowired
	private AccountFeign proxy;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of Cape
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllCommercial(int page, int size, String filter, Map<String, LocalDate> dates,
			ContractState contractState, PortageCompanies structureContract, ContractStatus contractStatus) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<CommercialContract> pagedResult = commercialeRepo.findAll(
				builder.filterSqlCommercial(filter, dates.get("startDate"), dates.get("endDate"), contractState, structureContract,
						contractStatus),
				paging);
		return createPagination(pagedResult);
	}

	/**
	 * Method to return all amendment on a contract in DB
	 * 
	 * @param contractId The the contract ID to look the amendment linked to.
	 * @return The list of Commercial Contract amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<CommercialContract> pagedResult = commercialeRepo.findAllAmendment(contractId, paging);
		return createPagination(pagedResult);
	}

	/**
	 * Method to return all contract in DB of an account
	 * 
	 * @param accountId ID of the account to extract the contract from
	 * @return The list of Commercial Contract
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllCommercialFromAccount(int page, int size, UUID accountId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<CommercialContract> pagedResult = commercialeRepo.findByAccountId(accountId, paging);
		return createPagination(pagedResult);
	}

	private Map<String, Object> createPagination(Page<CommercialContract> pagedResult) {
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
	 * @param contractId The public ID is an UUID linked to the accounts of the
	 *                   users
	 * @return An optional account, if found. The account will return all the linked
	 *         objects
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Optional<CommercialContract> getCommercialById(UUID contractId) {
		logger.info("Returning Commercial Contract with id {}", contractId);
		return commercialeRepo.findById(contractId);
	}

	/**
	 * This service will be used to create a CAPE object using the ID of the account
	 * to link it to.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory
	 *                       except for the contract id.
	 * @return The new updated contract object will be returned
	 * @throws CommercialContractException When an error is detected
	 * @throws PdfGeneratorException
	 */
	@Override
	@Transactional(rollbackFor = { CommercialContractException.class })
	public CommercialContract createNewCommercial(CommercialCreate contractPublic)
			throws CommercialContractException, PdfGeneratorException {
		logger.info("Creating a new Commercial Contract");
		CommercialContract contract = new CommercialContract().buildCommercial(contractPublic);
		writtingFileAsPdf(contract, ContractsName.COMMERCIAL_CONTRACT_AUXIME.getFileName());
		return commercialeRepo.save(contract);
	}

	/**
	 * This service will be used to update a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object contractPublic with the fields mandatory.
	 * @return The new updated contract object will be returned
	 * @throws CommercialContractException When an error is detected
	 */
	@Override
	@Transactional(rollbackFor = { CommercialContractException.class })
	public CommercialContract updateCommercialFromId(CommercialUpdate contractPublic)
			throws CommercialContractException {
		logger.info("Updating Commercial Contract with id : {}", contractPublic.getContractId());
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractPublic.getContractId());
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		CommercialContract contract = contractOpt.get().buildCommercialCommon(contractPublic);
		return commercialeRepo.save(contract);
	}

	/**
	 * This service will be used to delete a contract object in the DB using the ID
	 * of the contract object.
	 * 
	 * @param contractPublic The object activityPublic with the fields mandatory
	 * @throws CommercialContractException When an error is raised if not found
	 */
	@Override
	@Transactional(rollbackFor = { CommercialContractException.class })
	public void deleteCommercial(UUID contractId) throws CommercialContractException {
		logger.info("Deleting Commercial Contract {}", contractId);
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractId);
		if (contractOpt.isEmpty()) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		logger.info("Activity is in DB and is being deleted");
		contractOpt.get().setStatus(false);
		commercialeRepo.save(contractOpt.get());
	}

	/**
	 * Create an addendum to a CAPE contract
	 * 
	 * @param contractPublic The object contract with the fields mandatory
	 * @return CommercialContract Contract the created object
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 * @throws PdfGeneratorException
	 */
	@Override
	public CommercialContract createAmendmentCommercial(CreateCommercialAmendment contractPublic)
			throws CommercialContractException, PdfGeneratorException {
		logger.info("Creat an amendment to CAPE {}", contractPublic.getContractAmendment());
		if (commercialeRepo.existsById(contractPublic.getContractAmendment())) {
			CommercialContract contract = new CommercialContract().buildCommercialAmend(contractPublic);
			writtingFileAsPdf(contract, ContractsName.COMMERCIAL_CONTRACT_AUXIME.getFileName());
			return commercialeRepo.save(contract);
		} else {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
	}

	/**
	 * Validate a contract in DB. Will generate the PDF.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	@Override
	public CommercialContract validateContract(UUID contractId) throws CommercialContractException {
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractId);
		if (contractOpt.isEmpty() || !contractOpt.get().getContractStatus().equals(ContractStatus.PENDING_VALIDATION)
				&& !contractOpt.get().getContractStatus().equals(ContractStatus.MODIFICATION_REQUIRED)) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		CommercialContract contract = contractOpt.get();
		contract.setContractStatus(ContractStatus.VALIDATED);
		return commercialeRepo.save(contract);
	}

	/**
	 * Refuse a contract in DB.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	@Override
	public CommercialContract refuseContract(UUID contractId) throws CommercialContractException {
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractId);
		if (contractOpt.isEmpty() || !contractOpt.get().getContractStatus().equals(ContractStatus.PENDING_VALIDATION)
				&& !contractOpt.get().getContractStatus().equals(ContractStatus.MODIFICATION_REQUIRED)) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		CommercialContract contract = contractOpt.get();
		contract.setContractStatus(ContractStatus.REFUSED);
		return commercialeRepo.save(contract);
	}

	/**
	 * Put a contract in waiting for validation by auxime.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	@Override
	public CommercialContract pendingValidationContract(UUID contractId) throws CommercialContractException {
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractId);
		if (contractOpt.isEmpty() || !contractOpt.get().getContractStatus().equals(ContractStatus.DRAFT)) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		CommercialContract contract = contractOpt.get();
		Optional<ProfileInfo> profileInfo = proxy.getProfilesFromAccountId(contract.getAccountId());
		if (profileInfo.isEmpty()) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NO_VALIDATOR);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NO_VALIDATOR);
		} else if (profileInfo.get().getManagerId() != null) {
			contract.setValidatorId(profileInfo.get().getManagerId());
		} else if (profileInfo.get().getBusinessManagerId() == null) {
			contract.setValidatorId(profileInfo.get().getBusinessManagerId());
		} else {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_INTERNAL_ERROR);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_INTERNAL_ERROR);
		}
		contract.setContractStatus(ContractStatus.PENDING_VALIDATION);
		return commercialeRepo.save(contract);
	}

	/**
	 * Ask for modification for a contract in DB.
	 * 
	 * @param contractId    The ID of the contract to change the status
	 * @param commentCreate The comment to ask the modification
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	@Override
	public CommercialContract modificationRequired(UUID contractId, CommentCommercialPublic commentCreate)
			throws CommercialContractException {
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractId);
		if (contractOpt.isEmpty() || !contractOpt.get().getContractStatus().equals(ContractStatus.PENDING_VALIDATION)) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		CommercialContract contract = contractOpt.get();
		contract.setContractStatus(ContractStatus.MODIFICATION_REQUIRED);
		CommentCommercialContract comment = new CommentCommercialContract()
				.buildCommentCommercialContract(commentCreate);
		contract.addComment(comment);
		return commercialeRepo.save(contract);
	}

	/**
	 * Adding a comment to answer.
	 * 
	 * @param contractId    The ID of the contract to change the status
	 * @param commentCreate The comment to ask the modification
	 * @return Updated CommercialContract
	 * @throws CommercialContractException When an error is thrown during the
	 *                                     process
	 */
	@Override
	public CommercialContract commentingContract(UUID contractId, CommentCommercialPublic commentCreate)
			throws CommercialContractException {
		Optional<CommercialContract> contractOpt = commercialeRepo.findById(contractId);
		if (contractOpt.isEmpty()
				|| !contractOpt.get().getContractStatus().equals(ContractStatus.MODIFICATION_REQUIRED)) {
			logger.error(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
			throw new CommercialContractException(ExceptionMessageConstant.COMMERCIAL_CONTRACT_NOT_FOUND);
		}
		CommercialContract contract = contractOpt.get();
		CommentCommercialContract comment = new CommentCommercialContract()
				.buildCommentCommercialContract(commentCreate);
		contract.addComment(comment);
		return commercialeRepo.save(contract);
	}

	@Override
	public Integer numberContracByStatus(UUID validatorId, boolean status, ContractStatus contractStatus) {
		return commercialeRepo.count(validatorId, status, contractStatus);
	}

	@Autowired
	private AccountFeign accountFeign;
	@Autowired
	private PdfGenerator pdfGenerator;

	private void writtingFileAsPdf(CommercialContract contract, String file) throws PdfGeneratorException {
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
