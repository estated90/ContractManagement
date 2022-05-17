package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Entity
@Table(name = "permanent_contract")
@AttributeOverride(name = "id", column = @Column(name = "permanent_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class PermanentContract extends Contract {

	private LocalDate ruptureDate;
	private LocalDate endDate;
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
