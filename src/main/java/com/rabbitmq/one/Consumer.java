package com.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * @Package:com.rabbitmq.one
 * @Auther:Brianwei
 * @date:2024/10/30:22:47
 * @discribe:
 */
public class Consumer {

	private static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.36.111");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			System.out.println(new String(message.getBody()));
		};
		CancelCallback cancelCallback = (a) -> {
			System.out.println("消息消费被中断");
		};
		String s = channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
		System.out.println("接收消息完毕");
		connection.close();
	}
}
