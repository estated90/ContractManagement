package com.auxime.contract.service;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.auxime.contract.model.Contract;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portage_contract")
@AttributeOverride(name = "id", column = @Column(name = "portage_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class PortageConvention extends Contract {
	@Column(name = "end_date")
	private LocalDate endDate;
}
