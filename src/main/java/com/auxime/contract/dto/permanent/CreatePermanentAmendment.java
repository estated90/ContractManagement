package com.auxime.contract.dto.permanent;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to create a permanent contract amendment
 * 
 * @author Nicolas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class CreatePermanentAmendment extends PermanentCreate {
	@NotNull
	private UUID contractAmendment;
}
