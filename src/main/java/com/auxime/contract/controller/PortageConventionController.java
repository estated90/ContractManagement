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

import com.auxime.contract.dto.portage.CreatePortageAmendment;
import com.auxime.contract.dto.portage.PortageCreate;
import com.auxime.contract.dto.portage.PortageUpdate;
import com.auxime.contract.exception.PortageConventionException;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.service.PortageConventionService;

/**
 * @author Nicolas
 *
 */
@RestController
@Validated
@RequestMapping("/api/contract/portageConvention")
public class PortageConventionController {

	private static final Logger logger = LogManager.getLogger(PortageConventionController.class);
	@Autowired
	private PortageConventionService portageService;

	/**
	 * 
	 * This controller is designed to return all the information of the
	 * PortageConvention.
	 * 
	 * @return A list of contract in DB
	 * 
	 */
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PortageConvention>> getAllCape() {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(portageService.getAllContract(), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all amendment of a PortageConvention.
	 * 
	 * @param contractId The Id of the contract to extract
	 * @return A list of contract in DB filtered
	 * 
	 */
	@GetMapping(value = "/listAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PortageConvention>> getListCommercialContractAmendment(
			@RequestParam @NotNull UUID contractId) {
		logger.info("Getting contracts with linked to {}", contractId);
		return new ResponseEntity<>(portageService.getAllAmendmentContract(contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all the information of the
	 * PortageConvention.
	 * 
	 * @param accountId The Id of the account to extract the contract from
	 * @return A list of contract in DB
	 * 
	 */
	@GetMapping(value = "/listAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PortageConvention>> extractAllCapeAccount(@RequestParam @NotNull UUID accountId) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(portageService.getAllContractFromAccount(accountId), HttpStatus.OK);
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
	public ResponseEntity<Optional<PortageConvention>> getContractById(@RequestParam @NotNull UUID contractId) {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(portageService.getContractById(contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to update a contract in DB. It will use the ID
	 * provided to update the correct contract.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws PortageConventionException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PortageConvention> updateContract(@RequestBody @Valid PortageUpdate contractPublic)
			throws PortageConventionException {
		logger.info("Updating contracts with id : {}", contractPublic.getContractId());
		return new ResponseEntity<>(portageService.updateContractFromId(contractPublic), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws PortageConventionException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PortageConvention> createContract(@RequestBody @Valid PortageCreate contractPublic)
			throws PortageConventionException {
		logger.info("Creating contracts");
		return new ResponseEntity<>(portageService.createNewContract(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new amendment in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for its
	 *                       creation
	 * @return A Permanent Contract object with the ID and infos
	 * @throws PortageConventionException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@PostMapping(value = "/createAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PortageConvention> createAmendmentContract(
			@RequestBody @Valid CreatePortageAmendment contractPublic) throws PortageConventionException {
		logger.info("Creating amendment contract");
		return new ResponseEntity<>(portageService.createPortageConventionContract(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws PortageConventionException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PortageConvention> deleteContract(@Valid @RequestBody PortageUpdate contractPublic)
			throws PortageConventionException {
		logger.info("Deleting contracts : {}", contractPublic.getContractId());
		portageService.deleteContract(contractPublic);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
