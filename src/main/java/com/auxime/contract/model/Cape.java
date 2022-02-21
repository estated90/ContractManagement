package com.auxime.contract.model;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="cape")
@AttributeOverride(name = "id", column = @Column(name = "cape_id"))
@Setter
@Getter
@NoArgsConstructor
public class Cape extends Contract{

	private LocalDateTime endDate;
	private boolean fse;
	@OneToOne(targetEntity = CommentsContract.class, cascade = CascadeType.ALL)
	private CommentsContract comment;
	
}
