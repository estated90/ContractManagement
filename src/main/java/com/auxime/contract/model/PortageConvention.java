package com.auxime.contract.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

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
@Setter
@Getter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("portage_convention")
public class PortageConvention extends Contract{

	@Column(name="commission")
	private int commission;
	
	public PortageConvention buildConventionCommon(PortagePublic contractPublic){
		this.setEndDate(contractPublic.getEndDate());
		this.build(contractPublic, ContractType.CONTRACT);
		this.createStateContract();
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
