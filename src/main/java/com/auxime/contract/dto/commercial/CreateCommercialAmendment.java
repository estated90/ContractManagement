package com.auxime.contract.dto.commercial;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to create a Commercial contract amendment
 * 
 * @author Nicolas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class CreateCommercialAmendment extends CommercialCreate {
	@NotNull
	private UUID contractAmendment;
}
