package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import com.auxime.contract.dto.permanent.CreatePermanentAmendment;
import com.auxime.contract.dto.permanent.PermanentCreate;
import com.auxime.contract.dto.permanent.PermanentPublic;
import com.auxime.contract.model.enums.ContractType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 *
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("permanent_contract")
public class PermanentContract extends Contract {

	private LocalDate ruptureDate;
	private boolean fse;
	private double hourlyRate;
	private double workTime;
	@OneToOne(targetEntity = CommentExit.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private CommentExit comment;

	public PermanentContract buildPermanentContract(PermanentCreate contractPublic) {
		this.buildPermanentCommon(contractPublic);
		this.build(contractPublic, ContractType.CONTRACT);
		this.setAccountId(contractPublic.getAccountId());
		return this;
	}

	public PermanentContract buildPermanentContractAmend(CreatePermanentAmendment contractPublic) {
		this.buildPermanentCommon(contractPublic);
		this.build(contractPublic, ContractType.AMENDMENT);
		this.setAccountId(contractPublic.getAccountId());
		this.setContractAmendment(contractPublic.getContractAmendment());
		return this;
	}

	public PermanentContract buildPermanentCommon(PermanentPublic contractPublic) {
		this.createStateContract(contractPublic.getEndDate());
		this.setRuptureDate((contractPublic.getRuptureDate()));
		this.setEndDate(contractPublic.getEndDate());
		this.setFse(contractPublic.getFse());
		this.setHourlyRate(contractPublic.getHourlyRate());
		this.setWorkTime(contractPublic.getWorkTime());
		return this;
	}
}
