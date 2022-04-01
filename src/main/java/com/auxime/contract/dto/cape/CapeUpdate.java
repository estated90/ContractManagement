package com.auxime.contract.dto.cape;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class used to update the CAPE
 * 
 * @author Nicolas
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class CapeUpdate extends CapePublic {

	@NotNull(message = "Contract Id cannot be null")
	private UUID contractId;
}
