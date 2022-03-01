package com.auxime.contract.dto.portage;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to create a portage convention amendment
 * 
 * @author Nicolas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class CreatePortageAmendment extends PortageCreate {
	@NotNull
	private UUID contractAmendment;
}
