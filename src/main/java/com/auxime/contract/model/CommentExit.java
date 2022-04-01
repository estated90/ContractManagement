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
@Table(name = "comment_exit")
@Setter
@Getter
@NoArgsConstructor
public class CommentExit extends Comments {

	private String motivesExit;

}
