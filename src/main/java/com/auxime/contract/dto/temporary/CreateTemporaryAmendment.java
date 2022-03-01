package com.auxime.contract.dto.temporary;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to create a Temporary contract amendment
 * 
 * @author Nicolas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class CreateTemporaryAmendment extends TemporaryCreate {
	@NotNull
	private UUID contractAmendment;
}
