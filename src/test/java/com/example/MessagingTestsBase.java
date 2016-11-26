package com.example;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class MessagingTestsBase {
	@Autowired
	MessageVerifier messaging;
	@Autowired
	CustomerHandler customerHandler;

	public void postCustomer() {
		Customer customer = new Customer("John", "john@example.com");
		customer.setId("foo");
		customerHandler.afterCreate(customer);
	}
}
