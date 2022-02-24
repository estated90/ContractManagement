package com.auxime.contract.utils;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.model.Cape;
import com.auxime.contract.repository.CapeRepository;

@Service
public class ContractUpdater {

	private static final Logger logger = LogManager.getLogger(ContractUpdater.class);
	@Autowired
	private CapeRepository capeRepo;

	@Async
	@Scheduled(cron = "0 0-59 12 * * *", zone = "Europe/Paris")
	public void contractStatusUpdater() {
		List<Cape> capes = capeRepo.findAll();
		capes.forEach(cape -> {
			if (cape.getContractState() == ContractState.CANCELED) {
				logger.info("No update to be done");
			} else {
				if (cape.getStartingDate().isAfter(LocalDate.now())) {
					cape.setContractState(ContractState.INACTIVE);
					capeRepo.save(cape);
				} else if (dateCheckerBetween(LocalDate.now(), cape.getStartingDate(), cape.getEndDate())) {
					cape.setContractState(ContractState.ACTIVE);
					capeRepo.save(cape);
				}
			}
		});

	}

	private boolean dateCheckerBetween(LocalDate dateToVerify, LocalDate startDate, LocalDate endDate) {
		if (startDate == null) {
			return endDate == null || dateToVerify.isBefore(endDate) || dateToVerify.isEqual(endDate);
		} else if (endDate == null) {
			return dateToVerify.isAfter(startDate) || dateToVerify.isEqual(startDate);
		} else {
			return dateToVerify.isAfter(startDate) && dateToVerify.isBefore(endDate) || dateToVerify.isEqual(startDate)
					|| dateToVerify.isEqual(endDate);
		}
	}

}
