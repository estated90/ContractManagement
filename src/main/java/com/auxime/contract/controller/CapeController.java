package com.auxime.contract.controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.dto.cape.CapeCreate;
import com.auxime.contract.dto.cape.CapeUpdate;
import com.auxime.contract.dto.cape.CreateCapeAmendment;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.model.Cape;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.service.CapeService;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Nicolas
 * @version 1.0.0
 *
 */
@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/cape")
public class CapeController {

	private static final Logger logger = LogManager.getLogger(CapeController.class);
	@Autowired
	private CapeService capeService;

	/**
	 * 
	 * This controller is designed to return all the information of the CAPE.
	 * 
	 * @return A List of Cape in DB
	 * 
	 */
	@GetMapping(value = "/listCape", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getAllCape(@RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size,
			@RequestParam(required = false) String filter, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) ContractState contractState, 
			@RequestParam(required = false) PortageCompanies structureContract, 
			@RequestParam(required = false) Integer rate) {
		logger.info("Getting contracts list");
		return new ResponseEntity<>(capeService.getAllCape(page, size, filter, startDate, endDate, contractState, structureContract, rate), HttpStatus.OK);
	}

	/**
	 * This controller is designed to return all amendment of a CAPE.
	 * 
	 * @param contractId The contract Id to find all related amendment
	 * @return A List of Cape in DB filtered
	 */
	@GetMapping(value = "/listCapeAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getListCapeAmendment(@RequestParam @NotNull UUID contractId,
			@RequestParam(defaultValue = "1") @Min(1) int page, @RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with linked to {}", contractId);
		return new ResponseEntity<>(capeService.getAllAmendmentContract(page, size, contractId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all the contract of an account.
	 * 
	 * @param accountId The Id of the account to extract the contract from
	 * @return A List Commercial Contract in DB
	 * 
	 */
	@GetMapping(value = "/listCapeAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> extractAllCapeAccount(@RequestParam @NotNull UUID accountId,
			@RequestParam(defaultValue = "1") @Min(1) int page, @RequestParam(defaultValue = "10") @Min(1) int size) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(capeService.getAllCapeFromAccount(page, size, accountId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to return all the information about a contract.
	 * It will allow the display of all these information in a designed form.
	 * 
	 * @param capeId the contract ID from which to extract all the cape from
	 * @return A ContractPublic, if found. The account will return all the linked
	 *         objects
	 */
	@GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Optional<Cape>> getContractById(@RequestParam @NotNull UUID capeId) {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(capeService.getContractById(capeId), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to update a contract in DB. It will use the ID
	 * provided to update the correct contract.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws CapeException An exception is raised if any problem is encountered
	 *                       when getting or reading the id
	 */
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cape> updateContract(@RequestBody @Valid CapeUpdate contractPublic) throws CapeException {
		logger.info("Updating contracts with id : {}", contractPublic.getContractId());
		return new ResponseEntity<>(capeService.updateContractFromId(contractPublic), HttpStatus.OK);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws Exception
	 */
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cape> createContract(@RequestBody @Valid CapeCreate contractPublic) throws Exception {
		logger.info("Creating contracts");
		return new ResponseEntity<>(capeService.createNewContract(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new amendment in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for its
	 *                       creation
	 * @return A Cape object with the ID and infos
	 * @throws CapeException An exception is raised if any problem is encountered
	 *                       when getting or reading the id
	 */
	@PostMapping(value = "/createAmendment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cape> createAmendmentContract(@RequestBody @Valid CreateCapeAmendment contractPublic)
			throws CapeException {
		logger.info("Creating amendment contract");
		return new ResponseEntity<>(capeService.createAmendmentCape(contractPublic), HttpStatus.CREATED);
	}

	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws CapeException An exception is raised if any problem is encountered
	 *                       when getting or reading the id
	 */
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cape> deleteContract(@Valid @RequestBody CapeUpdate contractPublic) throws CapeException {
		logger.info("Deleting contracts : {}", contractPublic.getContractId());
		capeService.deleteContract(contractPublic);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
