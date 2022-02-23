package com.auxime.contract.dto.permanent;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;

import com.auxime.contract.model.enums.PortageCompanies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@NotNull
	private UUID idAccount;
	@NotNull
	private boolean status;
	private LocalDate ruptureDate;
	private LocalDate endDate;
	@NotNull
	private boolean fse;
	@NotNull
	@DecimalMax("2")
	private double hourlyRate;
	@NotNull
	private double workTime;
}
