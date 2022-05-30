package com.auxime.contract.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.GenericGenerator;

import com.auxime.contract.constants.ContractState;
import com.auxime.contract.dto.ContractDto;
import com.auxime.contract.model.enums.ContractType;
import com.auxime.contract.model.enums.PortageCompanies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 * @version 1.0.0
 *
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Setter
@Getter
@Entity
@NoArgsConstructor
@DiscriminatorColumn(name = "contract_typology", discriminatorType = DiscriminatorType.STRING)
public abstract class Contract {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID contractId;
	@Column(name = "contract_typology", insertable = false, updatable = false)
    private String contractTypology;
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
	private boolean status = true;
	@Column(name = "contract_type")
	@Enumerated(EnumType.STRING)
	private ContractType contractType;
	@Column(name = "contract_amendment")
	private UUID contractAmendment;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	@Column(name = "end_date")
	private LocalDate endDate;

	/**
	 * Return if the date is between a starting date and an ending date
	 * 
	 * @param dateToVerify Date to use to calculate if true or false
	 * @param startDate    Starting date to evaluate
	 * @param endDate      Ending date to evaluate
	 * @return Boolean value that validate if the date is between the two bornes
	 */
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

	protected Contract build(ContractDto contractPublic, ContractType contractType) {
		if (createdAt == null) {
			this.setCreatedAt(LocalDateTime.now());
		} else {
			this.setUpdatedAt(LocalDateTime.now());
		}
		this.setContractType(contractType);
		this.setContractDate(contractPublic.getContractDate());
		this.setStartingDate(contractPublic.getStartingDate());
		this.setContractTitle(contractPublic.getContractTitle());
		this.setStructureContract(contractPublic.getStructureContract());
		return this;
	}

	/**
	 * Calculate the State of a contract and apply it
	 * 
	 * @return the Updated object
	 */
	public Contract createStateContract(LocalDate endDate) {
		if (this.getContractState() == ContractState.CANCELED) {
			this.setContractState(ContractState.CANCELED);
		} else {
			if (this.getStartingDate().isAfter(LocalDate.now())) {
				this.setContractState(ContractState.NOT_STARTED);
			} else if (dateCheckerBetween(LocalDate.now(), this.getStartingDate(), endDate)) {
				this.setContractState(ContractState.ACTIVE);
			} else if (LocalDate.now().isAfter(endDate)) {
				this.setContractState(ContractState.INACTIVE);
			}
		}
		return this;
	}
}
