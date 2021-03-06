package com.auxime.contract.dto.cape;

import javax.validation.constraints.NotNull;

import com.auxime.contract.dto.ContractDto;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object to communicate with all action on CAPE.
 * 
 * @author Nicolas
 *
 */
@Validated
@Getter
@Setter
@NoArgsConstructor
public class CapePublic extends ContractDto {

	@NotNull
	private Boolean fse;
}
