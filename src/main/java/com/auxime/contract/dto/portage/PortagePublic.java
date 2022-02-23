package com.auxime.contract.dto.portage;

import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.auxime.contract.model.enums.PortageCompanies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PortagePublic {
	
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
	@NotNull
	private LocalDate endDate;
}
