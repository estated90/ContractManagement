package com.auxime.contract.dto.permanent;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object used to create a Permanent contract in DB
 * 
 * @author Nicolas
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class PermanentCreate extends PermanentPublic {

	@NotNull(message = "Account Id cannot be null")
	private UUID accountId;

}
