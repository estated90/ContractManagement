package com.auxime.contract.model;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;

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

	private LocalDateTime ruptureDate;
	private LocalDateTime endDate;
	private boolean fse;
	@DecimalMax("2")
	private double hourlyRate;
	private double workTime;
	@OneToOne(targetEntity = CommentsContract.class, cascade = CascadeType.ALL)
	private CommentsContract comment;
}
