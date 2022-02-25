package com.auxime.contract.utils;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.auxime.contract.model.Cape;
import com.auxime.contract.model.CommercialContract;
import com.auxime.contract.model.PermanentContract;
import com.auxime.contract.model.PortageConvention;
import com.auxime.contract.model.TemporaryContract;
import com.auxime.contract.repository.CapeRepository;
import com.auxime.contract.repository.CommercialRepository;
import com.auxime.contract.repository.PermanentContractRepository;
import com.auxime.contract.repository.PortageConventionRepository;
import com.auxime.contract.repository.TemporaryContractRepository;

@Service
public class ContractUpdater {

	private static final Logger logger = LogManager.getLogger(ContractUpdater.class);
	@Autowired
	private CapeRepository capeRepo;
	@Autowired
	private CommercialRepository commercialRepo;
	@Autowired
	private PermanentContractRepository permanentRepo;
	@Autowired
	private PortageConventionRepository portageRepo;
	@Autowired
	private TemporaryContractRepository temporaryRepo;
	

	@Async
	@Scheduled(cron = "0 0 22 * * *", zone = "Europe/Paris")
	public void contractStatusUpdater() {
		logger.info("Updating the state of all contracts");
		capeUpdater();
		commercialUpdater();
		permanentUpdater();
		portageUpdater();
		temporaryUpdater();
	}
	
	public void capeUpdater() {
		List<Cape> capes = capeRepo.findAll();
		capes.forEach(cape -> {
			cape.createStateContract();
			capeRepo.save(cape);
		});
	}

	public void commercialUpdater() {
		List<CommercialContract> contracts = commercialRepo.findAll();
		contracts.forEach(contract -> {
			contract.createStateContract();
			commercialRepo.save(contract);
		});
	}
	
	public void permanentUpdater() {
		List<PermanentContract> contracts = permanentRepo.findAll();
		contracts.forEach(contract -> {
			contract.createStateContract();
			permanentRepo.save(contract);
		});
	}
	
	public void portageUpdater() {
		List<PortageConvention> contracts = portageRepo.findAll();
		contracts.forEach(contract -> {
			contract.createStateContract();
			portageRepo.save(contract);
		});
	}
	
	public void temporaryUpdater() {
		List<TemporaryContract> contracts = temporaryRepo.findAll();
		contracts.forEach(contract -> {
			contract.createStateContract();
			temporaryRepo.save(contract);
		});
	}
}