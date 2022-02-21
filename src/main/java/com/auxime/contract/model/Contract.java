package com.auxime.contract.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.model.enums.PortageCompanies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public abstract class Contract {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id;
	private LocalDate contractDate;
	private LocalDate startingDate;
	private String contractTitle;
	private PortageCompanies structureContract;
	private UUID idAccount;
	private String lastName;
	private String firstName;
	private ContractType contractType;
	private UUID contractAmendment;
	private LocalDate startAmendment;
	private LocalDate endAmendment;
	
}
