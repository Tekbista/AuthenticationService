package com.tekbista.authentication.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tekbista.authentication.entities.User;
import com.tekbista.authentication.events.RegistrationCompleteEvent;
import com.tekbista.authentication.models.EmailDetails;
import com.tekbista.authentication.security.JwtTokenHelper;
import com.tekbista.authentication.services.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{

	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		// Create the verification token for the user with link
		User user = event.getUser();
		String token = jwtTokenHelper.generateToken(user);
		
		String url = event.getApplicationURL() + "/api/v1/auth/verifyRegistration?token=" + token;		
		
		// Send verification email
		EmailDetails emailDetails = new EmailDetails();
		emailDetails.setRecipient(user.getEmail());
		emailDetails.setMsgBody(
				"<h4>Registration Success</h4>" +
				"<p>Please click the link below to verify your registration.</p>" +
				"<p>" + url + "</p>"
				);
		emailDetails.setSubject("Registration Success");
		
		emailService.sendEmail(emailDetails);
		log.info("Registration email sent.");
	}

}
