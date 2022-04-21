package com.auxime.contract.controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.dto.portage.CreatePortageAmendment;
import com.auxime.contract.dto.portage.PortageCreate;
import com.auxime.contract.dto.portage.PortageUpdate;
import com.auxime.contract.exception.PortageConventionException;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.service.PortageConventionService;

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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/portageConvention")
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
	public ResponseEntity<Map<String, Object>> getAllCape(@RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size,
			@RequestParam(required = false) String filter, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) ContractState contractState, 
			@RequestParam(required = false) PortageCompanies structureContract) {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(portageService.getAllContract(page, size, filter, startDate, endDate, contractState, structureContract), HttpStatus.OK);
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
	public ResponseEntity<Map<String, Object>> getListCommercialContractAmendment(
			@RequestParam @NotNull UUID contractId, @RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with linked to {}", contractId);
		return new ResponseEntity<>(portageService.getAllAmendmentContract(page, size, contractId), HttpStatus.OK);
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
	public ResponseEntity<Map<String, Object>> extractAllCapeAccount(@RequestParam @NotNull UUID accountId,
			@RequestParam(defaultValue = "1") @Min(1) int page, @RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(portageService.getAllContractFromAccount(page, size, accountId), HttpStatus.OK);
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
	 * @param contractId Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws PortageConventionException An exception is raised if any problem is
	 *                                    encountered when getting or reading the id
	 */
	@DeleteMapping(value = "/delete/{contractId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PortageConvention> deleteContract(@Valid  @PathVariable UUID contractId)
			throws PortageConventionException {
		logger.info("Deleting contracts : {}", contractId);
		portageService.deleteContract(contractId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
