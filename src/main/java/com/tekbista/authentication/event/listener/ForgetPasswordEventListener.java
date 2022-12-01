package com.tekbista.authentication.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tekbista.authentication.entities.User;
import com.tekbista.authentication.events.ForgetPasswordEvent;
import com.tekbista.authentication.models.EmailDetails;
import com.tekbista.authentication.repositories.UserRepository;
import com.tekbista.authentication.security.JwtTokenHelper;
import com.tekbista.authentication.services.EmailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ForgetPasswordEventListener implements ApplicationListener<ForgetPasswordEvent>{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenHelper tokenHelper;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public void onApplicationEvent(ForgetPasswordEvent event) {
		String email = event.getEmail();
		User user = userRepository.findByEmail(email);
		
		if(user != null) {
			// Generate token
			String token = tokenHelper.generateToken(user);
			String url = event.getPasswordResetUrl() + "/api/v1/passwordReset?token=" + token;
			// Send password reset email
			EmailDetails emailDetails = new EmailDetails();
			emailDetails.setRecipient(user.getEmail());
			emailDetails.setMsgBody(
					"<h4>Password Reset</h4>" +
					"<p>Please click the link below to reset your password.</p>" +
					"<p>" + url + "</p>"
					);
			emailDetails.setSubject("Password Reset");
			
			emailService.sendEmail(emailDetails);
			log.info("Password reset email sent to ." + email);
		}else {
			log.info("Password reset email could not sent to ." + email);
		}
		
	}

}
