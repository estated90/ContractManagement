package com.auxime.contract.dto.cape;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object used to create a CAPE in DB
 * 
 * @author Nicolas
 */
@Getter
@Setter
@NoArgsConstructor
public class CapeCreate extends CapePublic {

	@NotNull(message = "Portage Id cannot be null")
	private UUID accountId;

}
