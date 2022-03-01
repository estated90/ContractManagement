package com.auxime.contract.dto.cape;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class to create a CAPE amendment
 * 
 * @author Nicolas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class CreateCapeAmendment extends CapeCreate {
	@NotNull
	private UUID contractAmendment;
}
