package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomerHandlerTest {
	@Autowired
	MessageCollector messageCollector;
	@Autowired
	CustomerHandler customerHandler;
	@Autowired
	@Output(Source.OUTPUT)
	MessageChannel output;

	@Test
	@SuppressWarnings("unchecked")
	public void afterCreate() throws Exception {
		Customer customer = new Customer("John", "john@example.com");
		customer.setId("foo");
		customerHandler.afterCreate(customer);

		Message<CustomerCreateEvent> message = (Message<CustomerCreateEvent>) messageCollector
				.forChannel(output).poll(2, TimeUnit.SECONDS);
		assertThat(message.getPayload()).isEqualTo(
				"{\"id\":\"foo\",\"name\":\"John\",\"email\":\"john@example.com\"}");
	}

}