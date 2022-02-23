package com.auxime.contract.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.auxime.contract.constants.DurationUnit;

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

	@Column(name = "end_date")
	private LocalDate endDate;
	@Column(name = "client_id")
	private UUID clientId;
	@Column(name="global_amount")
	private double globalAmount;
	@Column(name="monthly_amount")
	private double monthlyAmount;
	@Column(name = "mission_duration")
	private int missionDuration;
	@Column(name = "duration_unit")
	@Enumerated(EnumType.STRING)
	private DurationUnit durationUnit;
}
