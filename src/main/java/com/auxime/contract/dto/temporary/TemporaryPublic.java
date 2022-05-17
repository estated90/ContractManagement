package com.auxime.contract.dto.temporary;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.auxime.contract.dto.ContractDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class TemporaryPublic extends ContractDto {

	private LocalDate ruptureDate;
	@NotNull
	private LocalDate endDate;
	@NotNull
	private Double hourlyRate;
	@NotNull
	private Double workTime;
}
