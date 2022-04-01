package com.auxime.contract.dto;

import javax.validation.constraints.NotNull;

import com.auxime.contract.model.enums.TypeRate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RateDto {

	@NotNull(message = "The rate cannot be null")
	private int rate;
	@NotNull(message = "The rate type cannot be null")
	private TypeRate typeRate;
}
