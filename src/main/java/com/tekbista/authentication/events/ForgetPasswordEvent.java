package com.tekbista.authentication.events;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPasswordEvent extends ApplicationEvent{

	
	private static final long serialVersionUID = 1L;
	private String email;
	private String passwordResetUrl;
	
	public ForgetPasswordEvent(String email, String passwordResetUrl) {
		super(email);
		this.email = email;
		this.passwordResetUrl = passwordResetUrl;
	}
}
