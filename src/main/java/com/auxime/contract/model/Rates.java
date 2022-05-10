package com.auxime.contract.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.auxime.contract.dto.RateDto;
import com.auxime.contract.model.enums.TypeRate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "rates")
public class Rates {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "rate_id")
	private UUID rateId;
	@Column(name = "rate")
	private Integer rate;
	@Column(name = "type_rate")
	private TypeRate typeRate;
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Rates build(RateDto rate) {
		if (createdAt == null) {
			this.setCreatedAt(LocalDateTime.now());
		} else {
			this.setUpdatedAt(LocalDateTime.now());
		}
		this.setRate(rate.getRate());
		this.setTypeRate(rate.getTypeRate());
		return this;
	}
}
