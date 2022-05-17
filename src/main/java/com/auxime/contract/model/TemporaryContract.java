package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Entity
@Table(name="temporary_contract")
@AttributeOverride(name = "id", column = @Column(name = "temporary_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class TemporaryContract extends Contract{

	private LocalDate ruptureDate;
	private LocalDate endDate;
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
