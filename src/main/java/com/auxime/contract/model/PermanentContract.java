package com.auxime.contract.model;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="permanent_contract")
@AttributeOverride(name = "id", column = @Column(name = "permanent_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class PermanentContract extends Contract{

	private LocalDate ruptureDate;
	private LocalDate endDate;
	private boolean fse;
	private double hourlyRate;
	private double workTime;
	@OneToOne(targetEntity = CommentsContract.class, cascade = CascadeType.ALL)
	private CommentsContract comment;
}
