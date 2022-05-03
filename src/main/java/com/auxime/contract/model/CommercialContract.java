package com.auxime.contract.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.constants.ContractStatus;
import com.auxime.contract.constants.DurationUnit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 * @version 1.0.0
 */
@Entity
@Table(name = "commercial_contract")
@AttributeOverride(name = "id", column = @Column(name = "commercial_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class CommercialContract extends Contract {

	@Column(name = "end_date")
	private LocalDate endDate;
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
	@OneToMany(targetEntity = CommentCommercialContract.class, cascade = CascadeType.ALL)
	private Set<CommentCommercialContract> comments = new HashSet<>();

	/**
	 * Calculate the State of a contract and apply it
	 * 
	 * @return the Updated object
	 */
	public CommercialContract createStateContract() {
		if (this.getContractState() == ContractState.CANCELED) {
		} else {
			if (this.getStartingDate().isAfter(LocalDate.now())) {
				this.setContractState(ContractState.NOT_STARTED);
			} else if (dateCheckerBetween(LocalDate.now(), this.getStartingDate(), this.getEndDate())) {
				this.setContractState(ContractState.ACTIVE);
			} else if (LocalDate.now().isAfter(this.getEndDate())) {
				this.setContractState(ContractState.INACTIVE);
			}
		}
		return this;
	}
	
	public void addComment(CommentCommercialContract comment) {
		this.comments.add(comment);
	}
}
