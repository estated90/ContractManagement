package com.auxime.contract.dto.portage;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PortageUpdate extends PortagePublic {

	@NotNull(message = "Contract Id cannot be null")
	private UUID contractId;
}
