package com.auxime.contract.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.auxime.contract.dto.temporary.CreateTemporaryAmendment;
import com.auxime.contract.dto.temporary.TemporaryCreate;
import com.auxime.contract.dto.temporary.TemporaryUpdate;
import com.auxime.contract.exception.TemporaryContractException;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.TemporaryContract;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.auxime.contract.service.TemporaryContractService;

/**
 * @author Nicolas
 *
 */
@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/temporaryContract")
public class TemporaryContractController {

	private static final Logger logger = LogManager.getLogger(TemporaryContractController.class);
	@Autowired
	private TemporaryContractService temporaryService;

	/**
	 * 
	 * This controller is designed to return all the information of the
	 * TemporaryContract.
	 * 
	 * @return A list of contract in DB
	 * 
	 */
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TemporaryContract>> getAllTemporaryContract(
			@RequestParam(defaultValue = "1") @Min(1) int page, @RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(temporaryService.getAllContract(page, size), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all amendment of a TemporaryContract.
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return A list of contract in DB filtered
	 * 
	 */
	@GetMapping(value = "/listAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TemporaryContract>> getListTemporaryContractAmendment(
			@RequestParam @NotNull UUID contractId, @RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with linked to {}", contractId);
		return new ResponseEntity<>(temporaryService.getAllAmendmentContract(page, size, contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all the information of the
	 * TemporaryContract.
	 * 
	 * @param accountId The Id of the account to extract the contract from
	 * @return A list of contract in DB
	 * 
	 */
	@GetMapping(value = "/listAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TemporaryContract>> extractAllTemporaryContractAccount(
			@RequestParam @NotNull UUID accountId, @RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(temporaryService.getAllContractFromAccount(page, size, accountId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all the information about a contract.
	 * It will allow the display of all these information in a designed form.
	 * 
	 * @param contractId The ID of the contract to extract
	 * @return A ContractPublic, if found. The account will return all the linked
	 *         objects
	 */
	@GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Optional<TemporaryContract>> getContractById(@RequestParam @NotNull UUID contractId) {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(temporaryService.getContractById(contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to update a contract in DB. It will use the ID
	 * provided to update the correct contract.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws TemporaryContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TemporaryContract> updateContract(@RequestBody @Valid TemporaryUpdate contractPublic)
			throws TemporaryContractException {
		logger.info("Updating contracts with id : {}", contractPublic.getContractId());
		return new ResponseEntity<>(temporaryService.updateContractFromId(contractPublic), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws TemporaryContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TemporaryContract> createContract(@RequestBody @Valid TemporaryCreate contractPublic)
			throws TemporaryContractException {
		logger.info("Creating contracts");
		return new ResponseEntity<>(temporaryService.createNewContract(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new amendment in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for its
	 *                       creation
	 * @return A Temporary Contract object with the ID and infos
	 * @throws TemporaryContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PostMapping(value = "/createAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TemporaryContract> createAmendmentContract(
			@RequestBody @Valid CreateTemporaryAmendment contractPublic) throws TemporaryContractException {
		logger.info("Creating amendment contract");
		return new ResponseEntity<>(temporaryService.createTemporaryContractAmendment(contractPublic),
				HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws TemporaryContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PortageConvention> deleteContract(@Valid @RequestBody TemporaryUpdate contractPublic)
			throws TemporaryContractException {
		logger.info("Deleting contracts : {}", contractPublic.getContractId());
		temporaryService.deleteContract(contractPublic);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
