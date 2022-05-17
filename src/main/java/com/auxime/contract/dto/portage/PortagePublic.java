package com.auxime.contract.dto.portage;

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
public class PortagePublic extends ContractDto {

	@NotNull
	private LocalDate endDate;
}
