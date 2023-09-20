package com.tekbista.authentication.services;

import org.springframework.stereotype.Service;

import com.tekbista.authentication.models.EmailDetails;

@Service
public interface KafkaProducerService {

	public void sendSignupMessage(EmailDetails details);
}
