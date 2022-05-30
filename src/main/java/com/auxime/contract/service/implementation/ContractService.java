package com.auxime.contract.service.implementation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.model.Contract;
import com.auxime.contract.model.enums.PortageCompanies;
import com.auxime.contract.repository.ContractRepository;

@Service
public class ContractService {

	@Autowired
	private ContractRepository contractRepo;

	public Map<String, Object> getAllContracts(@Min(1) int page, @Min(1) int size, String filter, LocalDate startDate,
			ContractState contractState, PortageCompanies structureContract) {
		Pageable paging = PageRequest.of(page - 1, size);
		Page<Contract> pagedResult = contractRepo.findAll(paging);
		return createPagination(pagedResult);
	}

	private Map<String, Object> createPagination(Page<Contract> pagedResult) {
		Map<String, Object> response = new HashMap<>();
		response.put("contracts", pagedResult.toList());
		response.put("currentPage", pagedResult.getNumber() + 1);
		response.put("totalItems", pagedResult.getTotalElements());
		response.put("totalPages", pagedResult.getTotalPages());
		return response;
	}
	
}
