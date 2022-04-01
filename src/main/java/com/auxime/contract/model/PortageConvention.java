package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.auxime.contract.constants.ContractState;

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
	
	/**
	 * Calculate the State of a contract and apply it
	 * 
	 * @return the Updated object
	 */
	public PortageConvention createStateContract() {
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
