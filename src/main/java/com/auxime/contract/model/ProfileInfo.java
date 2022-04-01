package com.auxime.contract.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProfileInfo {

	private String title;
	private String fistName;
	private String lastName;
	private LocalDate birthdate;
	private String birthPlace;
	private String birthCountry;
	private String nationality;
	private String socialSecurityNumber;
	private int number;
	private String complemement;
	private String street;
	private String addressComplement;
	private String zip;
	private String city;
	private String country;
	private String activity;

}
