package com.auxime.contract.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auxime.contract.dto.CommentCommercialPublic;
import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialUpdate;
import com.auxime.contract.dto.commercial.CreateCommercialAmendment;
import com.auxime.contract.exception.CommercialContractException;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.service.CommercialContractService;

/**
 * @author Nicolas
 *
 */
@RestController
@Validated
@RequestMapping("/api/contract/commercialContract")
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
	public ResponseEntity<List<CommercialContract>> getAllCape() {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(commercialService.getAllCommercial(), HttpStatus.OK);
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
	public ResponseEntity<List<CommercialContract>> getListCommercialContractAmendment(
			@RequestParam @NotNull UUID contractId) {
		logger.info("Getting contracts with linked to {}", contractId);
		return new ResponseEntity<>(commercialService.getAllAmendmentContract(contractId), HttpStatus.OK);
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
	public ResponseEntity<List<CommercialContract>> extractAllCapeAccount(@RequestParam @NotNull UUID accountId) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(commercialService.getAllCommercialFromAccount(accountId), HttpStatus.OK);
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
			throws CommercialContractException {
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
	 */
	@PostMapping(value = "/createAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> createAmendmentContract(
			@RequestBody @Valid CreateCommercialAmendment contractPublic) throws CommercialContractException {
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
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommercialContract> deleteContract(@Valid @RequestBody CommercialUpdate contractPublic)
			throws CommercialContractException {
		logger.info("Deleting contracts : {}", contractPublic.getContractId());
		commercialService.deleteCommercial(contractPublic);
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
}
