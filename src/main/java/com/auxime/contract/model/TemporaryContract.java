package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import com.auxime.contract.dto.temporary.CreateTemporaryAmendment;
import com.auxime.contract.dto.temporary.TemporaryCreate;
import com.auxime.contract.dto.temporary.TemporaryPublic;
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
@DiscriminatorValue("temporary_contract")
public class TemporaryContract extends Contract{

	private LocalDate ruptureDate;
	private double hourlyRate;
	private double workTime;
	@OneToOne(targetEntity = CommentExit.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private CommentExit comment;
	
	public TemporaryContract buildTemporary(TemporaryCreate contractPublic){
		this.setAccountId(contractPublic.getAccountId());
		this.buildTemporaryCommon(contractPublic);
		return this;
	}

	public TemporaryContract buildTemporary(CreateTemporaryAmendment contractPublic){
		this.setAccountId(contractPublic.getAccountId());
		this.setContractAmendment(contractPublic.getContractAmendment());
		this.buildTemporary(contractPublic);
		return this;
	}

	public TemporaryContract buildTemporaryCommon(TemporaryPublic contractPublic){
		this.build(contractPublic, ContractType.CONTRACT);
		this.setRuptureDate(contractPublic.getRuptureDate());
		this.setEndDate(contractPublic.getStartingDate().plusYears(1));
		this.setHourlyRate(contractPublic.getHourlyRate());
		this.setWorkTime(contractPublic.getWorkTime());
		return this;
	}
}
