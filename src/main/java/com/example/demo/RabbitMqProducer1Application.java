package com.example.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitMqProducer1Application {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMqProducer1Application.class, args);
	}

	@Bean
	public Queue getQueue() throws UnknownHostException {
		return new Queue(InetAddress.getLocalHost().getHostName());
	}

	@Bean
	public Queue getQueue1() {
		return new Queue("queue1");
	}

	@Bean
	public Queue getQueue2() {
		return new Queue("queue2");
	}

	@Bean
	public CachingConnectionFactory getConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setPort(5672);
		connectionFactory.setHost("localhost");
		connectionFactory.setVirtualHost("/");
		return connectionFactory;
	}

	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange("Testing");
	}

	@Bean
	public Binding binding() throws UnknownHostException {
		return BindingBuilder.bind(getQueue()).to(fanoutExchange());
	}

	@Bean
	public Binding binding1() throws UnknownHostException {
		return BindingBuilder.bind(getQueue1()).to(fanoutExchange());
	}

	@Bean
	public Binding binding2() {
		return BindingBuilder.bind(getQueue2()).to(fanoutExchange());
	}

	@Bean
	public MessageListenerContainer getListener() throws UnknownHostException {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(getConnectionFactory());
		simpleMessageListenerContainer.setQueues(getQueue());
		simpleMessageListenerContainer.setQueues(getQueue1());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
		return simpleMessageListenerContainer;
	}

}
