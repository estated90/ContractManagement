package com.auxime.contract.controller;

import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.constants.ContractsName;
import com.auxime.contract.constants.DurationUnit;

@RestController
@Validated
@RequestMapping("/enums")
public class EnumsController {

	private static final Logger logger = LogManager.getLogger(EnumsController.class);

	@GetMapping(value = "/contractsName", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getContractsName() {
		logger.info("Getting all contracts names and path as list");
		List<String> enumNames = Stream.of(ContractsName.values()).map(ContractsName::getFriendlyFrName)
				.toList();
		return new ResponseEntity<>(enumNames, HttpStatus.OK);
	}

	@GetMapping(value = "/contractsState", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getContractState() {
		logger.info("Getting all contracts state");
		List<String> enumNames = Stream.of(ContractState.values()).map(ContractState::name).toList();
		return new ResponseEntity<>(enumNames, HttpStatus.OK);
	}

	@GetMapping(value = "/contractStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getContractStatus() {
		logger.info("Getting all Contract Status");
		List<String> enumNames = Stream.of(ContractStatus.values()).map(ContractStatus::name).toList();
		return new ResponseEntity<>(enumNames, HttpStatus.OK);
	}

	@GetMapping(value = "/durationUnits", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getDurationUnit() {
		logger.info("Getting all Duration Unit");
		List<String> enumNames = Stream.of(DurationUnit.values()).map(DurationUnit::name).toList();
		return new ResponseEntity<>(enumNames, HttpStatus.OK);
	}

}
