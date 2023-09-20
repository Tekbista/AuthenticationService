package com.tekbista.authentication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tekbista.authentication.models.EmailDetails;

@Service

public class KafkaProducerServiceImpl implements KafkaProducerService {
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	private static final String TOPIC = "user-reg-confirm-topic";

	@Override
	public void sendSignupMessage(EmailDetails details) {
		try {
			kafkaTemplate.send(TOPIC, details);
		} catch (Exception e) {
			System.out.println(e);
		}
		

	}

}
