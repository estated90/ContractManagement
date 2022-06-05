package com.auxime.contract.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.constants.DurationUnit;
import com.auxime.contract.dto.commercial.CommercialCreate;
import com.auxime.contract.dto.commercial.CommercialPublic;
import com.auxime.contract.dto.commercial.CreateCommercialAmendment;
import com.auxime.contract.model.enums.ContractType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("commercial_contract")
public class CommercialContract extends Contract {

	@Column(name = "client_id")
	private UUID clientId;
	@Column(name = "global_amount")
	private double globalAmount;
	@Column(name = "monthly_amount")
	private double monthlyAmount;
	@Column(name = "mission_duration")
	private int missionDuration;
	@Column(name = "duration_unit")
	@Enumerated(EnumType.STRING)
	private DurationUnit durationUnit;
	@Column(name = "contract_status")
	@Enumerated(EnumType.STRING)
	private ContractStatus contractStatus;
	@Column(name = "validator_id")
	private UUID validatorId;
	@OneToMany(targetEntity = CommentCommercialContract.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<CommentCommercialContract> comments = new HashSet<>();
	
	public void addComment(CommentCommercialContract comment) {
		this.comments.add(comment);
	}

	public CommercialContract buildCommercial(CommercialCreate contractPublic) {
		this.build(contractPublic, ContractType.CONTRACT);
		this.buildCommercialCommon(contractPublic);
		this.setAccountId(contractPublic.getAccountId());
		this.setContractStatus(ContractStatus.DRAFT);
		return this;
	}

	public CommercialContract buildCommercialAmend(CreateCommercialAmendment contractPublic) {
		this.build(contractPublic, ContractType.CONTRACT);
		this.buildCommercial(contractPublic);
		this.setContractAmendment(contractPublic.getContractAmendment());
		return this;
	}

	public CommercialContract buildCommercialCommon(CommercialPublic contractPublic){
		this.build(contractPublic, ContractType.CONTRACT);
		this.setEndDate(contractPublic.getEndDate());
		this.setClientId(contractPublic.getClientId());
		this.setGlobalAmount(contractPublic.getGlobalAmount());
		this.setMonthlyAmount(contractPublic.getMonthlyAmount());
		this.setMissionDuration(contractPublic.getMissionDuration());
		this.setDurationUnit(contractPublic.getDurationUnit());
		this.setClientId(contractPublic.getClientId());
		this.createStateContract();
		return this;
	}
}
