package com.tekbista.authentication.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	
	@NotEmpty
	@Size(min = 2, max = 100)
	private String address1;
	
	@Size(min = 2, max = 100)
	private String address2;
	
	@NotEmpty
	@Size(min = 2, max = 100)
	private String city;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	private State state;
	
	@NotEmpty
	@Size(min = 2, max = 100)
	private String zipCode;
}
