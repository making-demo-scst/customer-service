package com.example;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Customer.class)
public class CustomerHandler {
	private final MessageChannel output;
	private final EntityManager entityManager;

	public CustomerHandler(@Output(Source.OUTPUT) MessageChannel output,
			EntityManager entityManager) {
		this.output = output;
		this.entityManager = entityManager;
	}

	@HandleAfterCreate
	public void afterCreate(Customer customer) {
		entityManager.flush(); // commit db before sending messages
		System.out.println("Created " + customer);
		CustomerCreateEvent event = new CustomerCreateEvent();
		BeanUtils.copyProperties(customer, event);
		output.send(MessageBuilder.withPayload(event).build());
	}
}
