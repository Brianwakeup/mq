package com.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Package:com.rabbitmq.test
 * @Auther:Brianwei
 * @date:2024/10/30:21:55
 * @discribe: 生产者，发消息
 */
public class Producer {

	private static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.36.111");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		//1.队列名称
		//2.消息是否进行持久化
		//3.消息是否共享
		//4.消息是否自动删除
		//5.其他参数
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		String message = "hello world";
		//1.发送到哪个交换机
		//2.路由的key值是哪个
		//3.其他参数信息
		//4.发送消息的消息体
		channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
		System.out.println("消息发送完毕");
		connection.close();
	}
}
