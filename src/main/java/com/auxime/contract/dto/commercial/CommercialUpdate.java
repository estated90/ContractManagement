package com.auxime.contract.dto.commercial;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class use to update a commercial contract
 * 
 * @author Nicolas
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class CommercialUpdate extends CommercialPublic {

	@NotNull(message = "Contract Id cannot be under 0")
	private UUID contractId;
}
