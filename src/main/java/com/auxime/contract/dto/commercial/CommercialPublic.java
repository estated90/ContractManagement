package com.auxime.contract.dto.commercial;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.auxime.contract.constants.DurationUnit;
import com.auxime.contract.model.enums.PortageCompanies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommercialPublic {
	
	@NotNull
	private LocalDate contractDate;
	@NotNull
	private LocalDate startingDate;
	@NotNull
	private String contractTitle;
	@NotNull
	private PortageCompanies structureContract;
	@NotNull
	private LocalDate endDate;
	@NotNull
	private UUID clientId;
	@NotNull
	private Double globalAmount;
	@NotNull
	private Double monthlyAmount;
	@NotNull
	private int missionDuration;
	@NotNull
	private DurationUnit durationUnit;
}
