package com.auxime.contract.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="commercial_contract")
@AttributeOverride(name = "id", column = @Column(name = "commercial_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class CommercialContract extends Contract{

	private LocalDateTime endDate;
	private UUID clientId;
	@DecimalMax("2")
	private double globalAmount;
	@DecimalMax("2")
	private double monthlyAmount;
	private int missionDuration;
}
