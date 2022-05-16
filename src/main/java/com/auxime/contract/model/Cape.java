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

	public Cape buildCape(CapeCreate contractPublic) {
		this.build(contractPublic, ContractType.CONTRACT);
		this.setEndDate(contractPublic.getStartingDate().plusYears(1));
		this.setFse(contractPublic.getFse());
		this.setAccountId(contractPublic.getAccountId());
		this.createStateContract(endDate);
		contractPublic.getRates().forEach(rateDto -> {
			this.addRate(new Rates().build(rateDto));
		});
		return this;
	}

	public Cape buildAmendment(CreateCapeAmendment contractPublic) {
		this.build(contractPublic, ContractType.AMENDMENT);
		this.setEndDate(contractPublic.getStartingDate().plusYears(1));
		this.setFse(contractPublic.getFse());
		this.setAccountId(contractPublic.getAccountId());
		this.createStateContract(endDate);
		contractPublic.getRates().forEach(rateDto -> {
			this.addRate(new Rates().build(rateDto));
		});
		this.setContractAmendment(contractPublic.getContractAmendment());
		return this;
	}

	public Cape buildCape(CapeUpdate contractPublic) {
		this.build(contractPublic, ContractType.CONTRACT);
		this.setEndDate(contractPublic.getStartingDate().plusYears(1));
		this.setFse(contractPublic.getFse());
		this.createStateContract(endDate);
		return this;
	}

	public void addRate(Rates rate) {
		this.rates.add(rate);
	}

	public void removeActivity(Rates rate) {
		this.rates.remove(rate);
	}
}
