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

import com.auxime.contract.dto.permanent.CreatePermanentAmendment;
import com.auxime.contract.dto.permanent.PermanentCreate;
import com.auxime.contract.dto.permanent.PermanentUpdate;
import com.auxime.contract.exception.PermanentContractException;
import com.auxime.contract.model.PermanentContract;
import com.auxime.contract.service.PermanentContractService;

/**
 * @author Nicolas
 *
 */
@RestController
@Validated
@RequestMapping("/api/contract/permanentContract")
public class PermanentContractController {

	private static final Logger logger = LogManager.getLogger(PermanentContractController.class);
	@Autowired
	private PermanentContractService permanentService;

	/**
	 * 
	 * This controller is designed to return all the information of the Permanent
	 * Contract.
	 * 
	 * @return A List of Permanent Contract in DB
	 * 
	 */
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PermanentContract>> getAllCape() {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(permanentService.getAllPermanentContract(), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all amendment of a PermanentContract.
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return A List Permanent Contract in DB filtered
	 * 
	 */
	@GetMapping(value = "/listAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PermanentContract>> getListCommercialContractAmendment(
			@RequestParam @NotNull UUID contractId) {
		logger.info("Getting contracts with linked to {}", contractId);
		return new ResponseEntity<>(permanentService.getAllAmendmentContract(contractId), HttpStatus.OK);
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
	public ResponseEntity<List<PermanentContract>> extractAllCapeAccount(@RequestParam @NotNull UUID accountId) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(permanentService.getAllPermanentContractFromAccount(accountId), HttpStatus.OK);
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
	public ResponseEntity<Optional<PermanentContract>> getContractById(@RequestParam @NotNull UUID contractId) {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(permanentService.getContractById(contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to update a contract in DB. It will use the ID
	 * provided to update the correct contract.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws PermanentContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PermanentContract> updateContract(@RequestBody @Valid PermanentUpdate contractPublic)
			throws PermanentContractException {
		logger.info("Updating contracts with id : {}", contractPublic.getContractId());
		return new ResponseEntity<>(permanentService.updateContractFromId(contractPublic), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws PermanentContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PermanentContract> createContract(@RequestBody @Valid PermanentCreate contractPublic)
			throws PermanentContractException {
		logger.info("Creating contracts");
		return new ResponseEntity<>(permanentService.createNewContract(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new amendment in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for its
	 *                       creation
	 * @return A Permanent Contract object with the ID and infos
	 * @throws PermanentContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PostMapping(value = "/createAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PermanentContract> createAmendmentContract(
			@RequestBody @Valid CreatePermanentAmendment contractPublic) throws PermanentContractException {
		logger.info("Creating amendment contract");
		return new ResponseEntity<>(permanentService.createPermanentContractAmendment(contractPublic),
				HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws PermanentContractException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PermanentContractException> deleteContract(@Valid @RequestBody PermanentUpdate contractPublic)
			throws PermanentContractException {
		logger.info("Deleting contracts : {}", contractPublic.getContractId());
		permanentService.deleteContract(contractPublic);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
