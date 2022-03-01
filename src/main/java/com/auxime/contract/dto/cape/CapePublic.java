package com.auxime.contract.dto.cape;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.auxime.contract.model.enums.PortageCompanies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object to communicate with all action on CAPE. 
 * @author Nicolas
 *
 */
@Validated
@Getter
@Setter
@NoArgsConstructor
public class CapePublic {

	@NotNull
	private LocalDate contractDate;
	@NotNull
	private LocalDate startingDate;
	@NotNull
	private String contractTitle;
	@NotNull
	private PortageCompanies structureContract;
	@NotNull
	private Boolean fse;
}
