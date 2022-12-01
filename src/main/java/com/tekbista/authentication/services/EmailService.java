package com.tekbista.authentication.services;

import com.tekbista.authentication.models.EmailDetails;

public interface EmailService {
	void sendEmail(EmailDetails emailDetails);
}
