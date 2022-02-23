package com.auxime.contract.dto.commercial;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommercialCreate  extends CommercialPublic {

	@NotNull(message = "Account Id cannot be null")
	private UUID accountId;

}
