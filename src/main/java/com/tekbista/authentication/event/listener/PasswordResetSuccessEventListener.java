package com.tekbista.authentication.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tekbista.authentication.events.PasswordResetSuccessEvent;
import com.tekbista.authentication.models.EmailDetails;
import com.tekbista.authentication.services.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PasswordResetSuccessEventListener implements ApplicationListener<PasswordResetSuccessEvent>{

	@Autowired
	private EmailService emailService;
	
	@Override
	public void onApplicationEvent(PasswordResetSuccessEvent event) {
		
		if(!event.getEmail().isEmpty() && !event.getMessage().isEmpty()) {
			EmailDetails emailDetails = new EmailDetails();
			emailDetails.setRecipient(event.getEmail());
			emailDetails.setSubject("Password Reset Success");
			emailDetails.setMsgBody(event.getMessage());
			emailDetails.setAttachment(null);
			emailService.sendEmail(emailDetails);
			log.info("Password reset success email sent to ." + emailDetails.getRecipient());
		}
		
	}

}
