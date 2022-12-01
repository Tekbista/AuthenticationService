package com.tekbista.authentication.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

	@NotEmpty
	@Size(min = 8, max = 20)
	private String password;
	@NotEmpty
	@Size(min = 8, max = 200)
	private String confirmPassword;
}
