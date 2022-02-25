package com.auxime.contract.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.auxime.contract.constants.ContractState;
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
	@Column(name = "contract_date")
	private LocalDate contractDate;
	@Column(name = "starting_date")
	private LocalDate startingDate;
	@Column(name = "contract_title")
	private String contractTitle;
	@Column(name = "structure_contract")
	@Enumerated(EnumType.STRING)
	private PortageCompanies structureContract;
	@Column(name = "contract_state")
	@Enumerated(EnumType.STRING)
	private ContractState contractState;
	@Column(name = "account_id")
	private UUID accountId;
	@Column(name = "status")
	private boolean status;
	@Column(name = "contract_type")
	@Enumerated(EnumType.STRING)
	private ContractType contractType;
	@Column(name = "contract_amendment")
	private UUID contractAmendment;
	@Column(name = "start_amendment")
	private LocalDate startAmendment;
	@Column(name = "end_amendment")
	private LocalDate endAmendment;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	protected boolean dateCheckerBetween(LocalDate dateToVerify, LocalDate startDate, LocalDate endDate) {
		if (startDate == null) {
			return endDate == null || dateToVerify.isBefore(endDate) || dateToVerify.isEqual(endDate);
		} else if (endDate == null) {
			return dateToVerify.isAfter(startDate) || dateToVerify.isEqual(startDate);
		} else {
			return dateToVerify.isAfter(startDate) && dateToVerify.isBefore(endDate) || dateToVerify.isEqual(startDate)
					|| dateToVerify.isEqual(endDate);
		}
	}
}
