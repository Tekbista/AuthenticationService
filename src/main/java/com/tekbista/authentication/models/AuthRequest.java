package com.tekbista.authentication.models;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

	@NotEmpty
	private String email;
	@NotEmpty
	private String password;
}
