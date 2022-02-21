package com.auxime.contract.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="portage_contract")
@AttributeOverride(name = "id", column = @Column(name = "portage_contract_id"))
@Setter
@Getter
@NoArgsConstructor
public class PortageConvention extends Contract{

}
