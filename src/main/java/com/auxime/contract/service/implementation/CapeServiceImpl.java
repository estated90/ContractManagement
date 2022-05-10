package com.auxime.contract.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.auxime.contract.builder.ContractsSpecification;
import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ExceptionMessageConstant;
import com.auxime.contract.dto.cape.CapeCreate;
import com.auxime.contract.dto.cape.CapeUpdate;
import com.auxime.contract.dto.cape.CreateCapeAmendment;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.ProfileInfo;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.proxy.AccountFeign;
import com.auxime.contract.repository.CapeRepository;
import com.auxime.contract.service.CapeService;
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
 * @version 1.0.0
 *
 */
@Service
@Transactional
public class CapeServiceImpl implements CapeService {

	private static final Logger logger = LogManager.getLogger(CapeServiceImpl.class);
	@Autowired
	private CapeRepository capeRepo;
	@Autowired
	private PdfGenerator pdfGenerator;
	@Autowired
	private ContractsSpecification builder;

	/**
	 * Method to return all contract in DB
	 * 
	 * @return The list of Cape
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllCape(int page, int size, String filter, LocalDate startDate, LocalDate endDate,
			ContractState contractState, PortageCompanies structureContract, Integer rate) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<Cape> pagedResult = capeRepo.findAll(
				builder.getAllCape(filter, startDate, endDate, contractState, structureContract, rate), paging);
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
	 * @param contractId The Id of the contract to extract
	 * @return The list of Cape amendment
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllAmendmentContract(int page, int size, UUID contractId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<Cape> pagedResult = capeRepo.findAllAmendment(contractId, paging);
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
	 * @param accountId ID of the account to extract the contract from
	 * @return The list of Cape
	 * 
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Map<String, Object> getAllCapeFromAccount(int page, int size, UUID accountId) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<Cape> pagedResult = capeRepo.findByAccountId(accountId, paging);
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
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = { CapeException.class })
	public Cape createNewContract(CapeCreate contractPublic) throws Exception {
		logger.info("Creating a new CAPE");
		Cape contract = new Cape().buildCape(contractPublic);
		pdfGenerator(contract, "CAPE VIERGE 2022.docx");
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
		Cape contract = capeVerifier(contractPublic.getContractId());
		logger.info(ExceptionMessageConstant.CAPE_FOUND);
		contract = contract.buildCape(contractPublic);
		return capeRepo.save(contract);
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
	public void deleteContract(UUID contractId) throws CapeException {
		logger.info("Deleting a CAPE {}", contractId);
		Cape cape = capeVerifier(contractId);
		cape.setStatus(false);
		capeRepo.save(cape);
	}

	private Cape capeVerifier(UUID contractId) throws CapeException {
		logger.info("Deleting a CAPE {}", contractId);
		Optional<Cape> contractOpt = capeRepo.findById(contractId);
		if (contractOpt.isPresent() && contractOpt.get().isStatus()) {
			return contractOpt.get();
		} else {
			logger.error(ExceptionMessageConstant.CAPE_NOT_FOUND);
			throw new CapeException(ExceptionMessageConstant.CAPE_NOT_FOUND);
		}
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
			Cape cape = new Cape().buildAmendment(contract);
			return capeRepo.save(cape);
		} else {
			logger.error(ExceptionMessageConstant.CAPE_NOT_FOUND);
			throw new CapeException(ExceptionMessageConstant.CAPE_NOT_FOUND);
		}
	}

	@Autowired
	private AccountFeign accountFeign;

	private void pdfGenerator(Cape cape, String file) throws PdfGeneratorException {
		// Getting the info linked to the profile of contract account
		ProfileInfo profileInfo = accountFeign.getProfilesFromAccountId(cape.getAccountId());
		if (profileInfo == null) {
			logger.error(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
			throw new PdfGeneratorException(ExceptionMessageConstant.PROFILE_NOT_RETRIEVED);
		}
		Map<String, String> listWords = GenerateListVariable.setListVariable(cape, profileInfo);
		String fileName = cape.getContractType().toString() + " CAPE " + profileInfo.getLastName() + " "
				+ profileInfo.getFistName() + " " + LocalDateTime.now().toString().replace("-", "_").replace(":", "_");
		pdfGenerator.replaceTextModel(listWords, fileName, file);
	}
}
