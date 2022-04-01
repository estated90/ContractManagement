package com.auxime.contract.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentCommercialPublic {

	@NotNull
	private String comment;
	@NotNull
	private String lastName;
	@NotNull
	private String firstName;
	
}
