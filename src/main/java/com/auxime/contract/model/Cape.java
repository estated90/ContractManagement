package com.auxime.contract.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.auxime.contract.constants.ContractState;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 * @version 1.0.0
 *
 */
@Entity
@Table(name = "cape")
@AttributeOverride(name = "id", column = @Column(name = "cape_id"))
@Setter
@Getter
@NoArgsConstructor
public class Cape extends Contract {

	private LocalDate endDate;
	private boolean fse;
	@OneToOne(targetEntity = CommentExit.class, cascade = CascadeType.ALL)
	private CommentExit comment;
	@OneToMany(orphanRemoval = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<Rates> rates = new ArrayList<>();

	/**
	 * Calculate the State of a contract and apply it
	 * 
	 * @return the Updated object
	 */
	public Cape createStateContract() {
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
	
	public void addRate(Rates rate) {
		this.rates.add(rate);
	}
	
	public void removeActivity(Rates rate) {
		this.rates.remove(rate);
	}
}
