package com.tekbista.authentication.events;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetSuccessEvent extends ApplicationEvent{

	
	private static final long serialVersionUID = 1L;
	private String email;
	private String message;
	
	public PasswordResetSuccessEvent(String email, String message) {
		super(email);
		this.email = email;
		this.message = message;
	}
}
