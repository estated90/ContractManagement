package com.auxime.contract.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.dto.cape.CapeCreate;
import com.auxime.contract.dto.cape.CapeUpdate;
import com.auxime.contract.dto.cape.CreateCapeAmendment;
import com.auxime.contract.model.enums.ContractType;

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
	private Set<Rates> rates = new HashSet<>();

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

	public Cape buildCape(CapeCreate contractPublic) {
		this.build(contractPublic, ContractType.CONTRACT, true);
		this.setEndDate(contractPublic.getStartingDate().plusYears(1));
		this.setFse(contractPublic.getFse());
		this.setAccountId(contractPublic.getAccountId());
		this.createStateContract();
		contractPublic.getRates().forEach(rateDto -> {
			this.addRate(new Rates().build(rateDto));
		});
		return this;
	}

	public Cape buildAmendment(CreateCapeAmendment contractPublic) {
		this.build(contractPublic, ContractType.AMENDMENT, true);
		this.setEndDate(contractPublic.getStartingDate().plusYears(1));
		this.setFse(contractPublic.getFse());
		this.setAccountId(contractPublic.getAccountId());
		this.createStateContract();
		contractPublic.getRates().forEach(rateDto -> {
			this.addRate(new Rates().build(rateDto));
		});
		this.setContractAmendment(contractPublic.getContractAmendment());
		return this;
	}

	public Cape buildCape(CapeUpdate contractPublic) {
		this.build(contractPublic, ContractType.CONTRACT, true);
		this.setEndDate(contractPublic.getStartingDate().plusYears(1));
		this.setFse(contractPublic.getFse());
		this.createStateContract();
		return this;
	}

	public void addRate(Rates rate) {
		this.rates.add(rate);
	}

	public void removeActivity(Rates rate) {
		this.rates.remove(rate);
	}
}
