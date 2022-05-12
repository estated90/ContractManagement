package com.auxime.contract.dto.commercial;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.auxime.contract.constants.DurationUnit;
import com.auxime.contract.dto.ContractDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Common class of the commercial contract for API use
 * @author Nicolas
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class CommercialPublic extends ContractDto {
	
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
