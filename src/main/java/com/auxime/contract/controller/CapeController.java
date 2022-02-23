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

import com.auxime.contract.dto.cape.CapeCreate;
import com.auxime.contract.dto.cape.CapeUpdate;
import com.auxime.contract.exception.CapeException;
import com.auxime.contract.model.Cape;
import com.auxime.contract.service.CapeService;

@RestController
@Validated
@RequestMapping("/api/contract/cape")
public class CapeController {
	
	private static final Logger logger = LogManager.getLogger(CapeController.class);
	@Autowired
	private CapeService capeService;

	/**
	 * 
	 * This controller is designed to return all the information of the CAPE.
	 * 
	 * @return A List<Cape> in DB 
	 * 
	 */
	@GetMapping(value = "/listCape", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cape>> getAllCape() {
		logger.info("Getting contracts with id");
		return new ResponseEntity<>(capeService.getAllCape(), HttpStatus.OK);
	} 
	
	/**
	 * 
	 * This controller is designed to return all the information of the CAPE.
	 * 
	 * @return A List<Cape> in DB 
	 * 
	 */
	@GetMapping(value = "/listCapeAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cape>> extractAllCapeAccount(@RequestParam @NotNull UUID accountId) {
		logger.info("Getting contracts with id : {}", accountId);
		return new ResponseEntity<>(capeService.getAllCapeFromAccount(accountId), HttpStatus.OK);
	} 
	
	
	/**
	 * 
	 * This controller is designed to return all the information about a contract.
	 * It will allow the display of all these information in a designed form.
	 * 
	 * @param publicId The public ID is an int linked to the contract of the users
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
	 * This controller is designed to update a contract in DB.
	 * It will use the ID provided to update the correct contract.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws CapeException 
	 * @throws ContractException An exception is raised if any problem is encountered when getting or reading the id
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
	 * @throws ContractException An exception is raised if any problem is encountered when getting or reading the id
	 */
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cape> createContract(@RequestBody @Valid CapeCreate contractPublic)
			throws CapeException {
		logger.info("Creating contracts");
		return new ResponseEntity<>(capeService.createNewContract(contractPublic), HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * This controller is designed to create a new contract in DB.
	 * 
	 * @param contractPublic Object with all the field of the contract for update
	 * @return A contract object with the ID and infos
	 * @throws ContractException An exception is raised if any problem is encountered when getting or reading the id
	 * @throws ActivityException 
	 */
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cape> deleteContract(@Valid @RequestBody CapeUpdate contractPublic)
			throws CapeException {
		logger.info("Deleting contracts : {}", contractPublic.getContractId());
		capeService.deleteContract(contractPublic);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
