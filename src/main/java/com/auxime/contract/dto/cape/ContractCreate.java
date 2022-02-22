package com.auxime.contract.dto.cape;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContractCreate  extends CapePublic {

	@Min(value = 1, message = "Portage Id cannot be under 0")
	private int portageId;

}
