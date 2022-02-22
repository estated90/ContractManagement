package com.auxime.contract.dto.cape;

import java.util.UUID;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContractUpdate extends CapePublic {

	@Min(value = 1, message = "Contract Id cannot be under 0")
	private UUID contractId;
}
