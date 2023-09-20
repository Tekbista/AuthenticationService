package com.tekbista.authentication.services;


import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tekbista.authentication.models.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username")
	private String sender;
	
	@Override
	public void sendEmail(EmailDetails emailDetails) {
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			boolean html = true;
			
			helper.setFrom(sender);
			helper.setTo(emailDetails.getRecipient());
			helper.setText(
					emailDetails.getMsgBody()
					, html);
			helper.setSubject(emailDetails.getSubject());
			javaMailSender.send(message);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
