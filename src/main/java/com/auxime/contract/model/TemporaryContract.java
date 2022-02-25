package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;

import com.auxime.contract.constants.ContractState;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@OneToOne(targetEntity = CommentsContract.class, cascade = CascadeType.ALL)
	private CommentsContract comment;
	
	public TemporaryContract createStateContract() {
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
}
