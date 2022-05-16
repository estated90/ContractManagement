package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.auxime.contract.dto.portage.CreatePortageAmendment;
import com.auxime.contract.dto.portage.PortageCreate;
import com.auxime.contract.dto.portage.PortagePublic;
import com.auxime.contract.model.enums.ContractType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 *
 */
@Entity
@Table(name="commercial_contract")
@AttributeOverride(name = "id", column = @Column(name = "commercial_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class PortageConvention extends Contract{

	@Column(name = "end_date")
	private LocalDate endDate;
	@Column(name="commission")
	private int commission;
	
	public PortageConvention buildConventionCommon(PortagePublic contractPublic){
		this.build(contractPublic, ContractType.CONTRACT);
		this.setEndDate(contractPublic.getEndDate());
		return this;
	}

	public PortageConvention buildConvention(PortageCreate contractPublic){
		this.buildConventionCommon(contractPublic);
		this.setAccountId(contractPublic.getAccountId());
		return this;
	}

	public PortageConvention buildConvention(CreatePortageAmendment contractPublic){
		this.buildConventionCommon(contractPublic);
		this.setAccountId(contractPublic.getAccountId());
		this.setContractAmendment(contractPublic.getContractAmendment());
		return this;
	}
}
