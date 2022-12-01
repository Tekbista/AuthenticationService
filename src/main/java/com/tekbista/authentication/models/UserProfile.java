package com.tekbista.authentication.models;

import com.tekbista.authentication.entities.State;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserProfile {

	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private State state;
	private String zipCode;
	private String phone;

	

}
