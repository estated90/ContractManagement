package com.auxime.contract.dto.permanent;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.auxime.contract.model.enums.PortageCompanies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object to communicate with all action on Permanent contract.
 * @author Nicolas
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class PermanentPublic {
	
	@NotNull
	private LocalDate contractDate;
	@NotNull
	private LocalDate startingDate;
	@NotNull
	private String contractTitle;
	@NotNull
	private PortageCompanies structureContract;
	private LocalDate ruptureDate;
	private LocalDate endDate;
	@NotNull
	private Boolean fse;
	@NotNull
	private Double hourlyRate;
	@NotNull
	private Double workTime;
}
