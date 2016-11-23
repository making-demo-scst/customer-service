package com.example;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@SpringBootApplication
@EnableBinding(Source.class)
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	// Workarround http://stackoverflow.com/a/30713264/5861829
	@Configuration
	static class TxConfig {
		private final PlatformTransactionManager transactionManager;

		public TxConfig(PlatformTransactionManager transactionManager) {
			this.transactionManager = transactionManager;
		}

		@Bean
		TransactionInterceptor txAdvice() {
			NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
			source.addTransactionalMethod("post*", new DefaultTransactionAttribute());
			source.addTransactionalMethod("put*", new DefaultTransactionAttribute());
			source.addTransactionalMethod("delete*", new DefaultTransactionAttribute());
			source.addTransactionalMethod("patch*", new DefaultTransactionAttribute());
			return new TransactionInterceptor(transactionManager, source);
		}

		@Bean
		Advisor txAdvisor() {
			AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
			pointcut.setExpression(
					"execution(* org.springframework.data.rest.webmvc.RepositoryEntityController.*(..))");
			return new DefaultPointcutAdvisor(pointcut, txAdvice());
		}
	}
}
