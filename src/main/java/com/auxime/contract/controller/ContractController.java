package com.auxime.contract.controller;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.service.implementation.ContractService;

/**
 * @author Nicolas
 * @version 1.0.0
 *
 */
@RestController
@Validated
@RequestMapping("/contract")
public class ContractController {

	private static final Logger logger = LogManager.getLogger(ContractController.class);
	@Autowired
	private ContractService contractService;

	/**
	 * 
	 * This controller is designed to return all the information of the CAPE.
	 * 
	 * @return A List of Cape in DB
	 * 
	 */
	@GetMapping(value = "/listContracts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getAllContractsTypes(@RequestParam(defaultValue = "1") @Min(1) int page,
			@RequestParam(defaultValue = "10") @Min(1) int size,
			@RequestParam(required = false) String filter,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) ContractState contractState,
			@RequestParam(required = false) PortageCompanies structureContract) {
		logger.info("Getting contracts list");
		return new ResponseEntity<>(
				contractService.getAllContracts(page, size, filter, startDate, contractState, structureContract),
				HttpStatus.OK);
	}

}
