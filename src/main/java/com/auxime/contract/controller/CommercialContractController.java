package com.auxime.contract.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.dto.CommentCommercialPublic;
import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialUpdate;
import com.auxime.contract.dto.commercial.CreateCommercialAmendment;
import com.auxime.contract.exception.CommercialContractException;
import com.auxime.contract.exception.PdfGeneratorException;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.service.CommercialContractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nicolas
 *
 */
@RestController
@Validated
@RequestMapping("/commercialContract")
public class CommercialContractController {

	private static final Logger logger = LogManager.getLogger(CommercialContractController.class);
	@Autowired
	private CommercialContractService commercialService;

	/**
	 * 
	 * This controller is designed to return all the information of the
	 * CommercialContract.
	 * 
	 * @return A List Commercial Contract in DB
	 * 
	 */
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getAllCape(@RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size,
			@RequestParam(required = false) String filter,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) ContractState contractState,
			@RequestParam(required = false) PortageCompanies structureContract,
			@RequestParam(required = false) ContractStatus contractStatus) {
		logger.info("Getting all commercial contracts");
		Map<String, LocalDate> dates = new HashMap<>();
		dates.put("startDate", startDate);
		dates.put("endDate", endDate);
		return new ResponseEntity<>(commercialService.getAllCommercial(page, size, filter, dates,
				contractState, structureContract, contractStatus), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all amendment of a CommercialContract.
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return A List Commercial Contract in DB filtered
	 * 
	 */
	@GetMapping(value = "/listAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getListCommercialContractAmendment(
			@RequestParam @NotNull UUID contractId, @RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with linked to {}", contractId);
		return new ResponseEntity<>(commercialService.getAllAmendmentContract(page, size, contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all the contract of an account.
	 * 
	 * @param accountId The Id of the account to extract the contract from
	 * @return A List Commercial Contract in DB
	 * 
	 */
	@GetMapping(value = "/listAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> extractAllCapeAccount(@RequestParam @NotNull UUID accountId,
			@RequestParam(defaultValue = "1") @Min(1) int page, @RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(commercialService.getAllCommercialFromAccount(page, size, accountId),
				HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all the information about a contract.
	 * It will allow the display of all these information in a designed form.
	 * 
	 * @param capeId The ID of the contract to extract
	 * @return A ContractPublic, if found. The account will return all the linked
	 *         objects
	 */
	@GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Optional<CommercialContract>> getContractById(@RequestParam @NotNull UUID accountId) {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(commercialService.getCommercialById(accountId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to update a contract in DB. It will use the ID
	 * provided to update the correct contract.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> updateContract(@RequestBody @Valid CommercialUpdate contractPublic)
			throws CommercialContractException {
		logger.info("Updating contracts with id : {}", contractPublic.getContractId());
		return new ResponseEntity<>(commercialService.updateCommercialFromId(contractPublic), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> createContract(@RequestBody @Valid CommercialCreate contractPublic)
			throws CommercialContractException, PdfGeneratorException {
		logger.info("Creating contracts");
		return new ResponseEntity<>(commercialService.createNewCommercial(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new amendment in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for its
	 *                       creation
	 * @return A Permanent Contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 * @throws PdfGeneratorException
	 */
	@PostMapping(value = "/createAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> createAmendmentContract(
			@RequestBody @Valid CreateCommercialAmendment contractPublic) throws CommercialContractException, PdfGeneratorException {
		logger.info("Creating amendment contract");
		return new ResponseEntity<>(commercialService.createAmendmentCommercial(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@DeleteMapping(value = "/delete/{contractId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> deleteContract(@Valid @PathVariable UUID contractId)
			throws CommercialContractException {
		logger.info("Deleting contracts : {}", contractId);
		commercialService.deleteCommercial(contractId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 
	 * Controller to ask modification to a contract. Will have a feed of comments.
	 * 
	 * @param contractId    The ID of the contract to change the status
	 * @param commentCreate The comment to ask the modification
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@PutMapping(value = "/askModification", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> askModificationToUser(@RequestParam @Valid UUID contractId,
			@RequestBody @Valid CommentCommercialPublic commentCreate) throws CommercialContractException {
		logger.info("Asking modification on contract id : {}", contractId);
		return new ResponseEntity<>(commercialService.modificationRequired(contractId, commentCreate), HttpStatus.OK);
	}

	/**
	 * 
	 * Controller to ask for validation for a contract.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@PutMapping(value = "/pendingValidation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> askForValidation(@RequestParam @Valid UUID contractId)
			throws CommercialContractException {
		logger.info("Asking validation for contract id : {}", contractId);
		return new ResponseEntity<>(commercialService.pendingValidationContract(contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * Controller to refuse a contract. It will not be allowed to update it after.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@PutMapping(value = "/refuseContract", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> refuseContractDraft(@RequestParam @Valid UUID contractId)
			throws CommercialContractException {
		logger.info("Refusal for contract id : {}", contractId);
		return new ResponseEntity<>(commercialService.refuseContract(contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * Controller to validate a contract. It will not be allowed to update it after.
	 * 
	 * @param contractId The ID of the contract to change the status
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@PutMapping(value = "/validateContract", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> validateContractDraft(@RequestParam @Valid UUID contractId)
			throws CommercialContractException {
		logger.info("Validation for contract id : {}", contractId);
		return new ResponseEntity<>(commercialService.validateContract(contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * Controller to add comments to a contract.
	 * 
	 * @param contractId    The ID of the contract to change the status
	 * @param commentCreate The comment to ask the modification
	 * @return A contract object with the ID and infos
	 * @throws CommercialContractException An exception is raised if any problem is
	 *                                     encountered when getting or reading the
	 *                                     id
	 */
	@PutMapping(value = "/addComment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> addCommentToContract(@RequestParam @Valid UUID contractId,
			@RequestBody @Valid CommentCommercialPublic commentCreate) throws CommercialContractException {
		logger.info("Adding comment on contract id : {}", contractId);
		return new ResponseEntity<>(commercialService.commentingContract(contractId, commentCreate), HttpStatus.OK);
	}


	@GetMapping(value = "/myContractCount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> getNumberContractToModify(@RequestParam @NotNull UUID userId, @RequestParam @NotNull boolean status, @RequestParam @NotNull ContractStatus contractStatus) {
		logger.info("Getting the number of contract to modify from an account");
		return new ResponseEntity<>(commercialService.numberContracByStatus(userId, status, contractStatus), HttpStatus.OK);
	}
}
