package com.auxime.contract.dto.permanent;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class used to update the Permanent contract
 * @author Nicolas
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class PermanentUpdate extends PermanentPublic {

	@NotNull(message = "Contract Id cannot be null")
	private UUID contractId;
}
