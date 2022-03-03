package com.auxime.contract.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nicolas
 *
 */
@Entity
@Table(name = "comment_commercial")
@Setter
@Getter
@NoArgsConstructor
public class CommentCommercialContract extends Comments {

	private String lastName;
	private String firstName;

}
