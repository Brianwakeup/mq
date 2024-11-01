package com.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Package:com.rabbitmq.utils
 * @Auther:Brianwei
 * @date:2024/10/31:20:35
 * @discribe:
 */
public class RabbitMqUtil {

	public static Channel getChannel(){
		try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setHost("192.168.36.111");
			connectionFactory.setUsername("admin");
			connectionFactory.setPassword("admin");
			Connection connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();
			return channel;
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
}
