package com.tekbista.authentication.models;

import com.tekbista.authentication.entities.Address;

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
	private Address address;
	private String phone;

	

}
