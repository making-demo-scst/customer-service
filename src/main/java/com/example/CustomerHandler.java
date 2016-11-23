package com.example;

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

	public CustomerHandler(@Output(Source.OUTPUT) MessageChannel output) {
		this.output = output;
	}

	@HandleAfterCreate
	public void afterCreate(Customer customer) {
		System.out.println("Created " + customer);
		CustomerCreateEvent event = new CustomerCreateEvent();
		BeanUtils.copyProperties(customer, event);
		output.send(MessageBuilder.withPayload(event).build());
	}
}
