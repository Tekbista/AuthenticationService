package com.tekbista.authentication.events;

import org.springframework.context.ApplicationEvent;

import com.tekbista.authentication.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	private User user;
	private String applicationURL;

	public RegistrationCompleteEvent(User user, String applicaitonURL) {
		super(user);
		this.user  = user;
		this.applicationURL = applicaitonURL;
	}

}
