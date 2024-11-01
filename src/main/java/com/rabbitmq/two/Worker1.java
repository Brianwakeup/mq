package com.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;

/**
 * @Package:com.rabbitmq.two
 * @Auther:Brianwei
 * @date:2024/10/31:20:35
 * @discribe:
 */
public class Worker1 {

	private static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			System.out.println("接收到消息：" + new String(message.getBody()));
		};
		CancelCallback cancelCallback = (a) -> {
			System.out.println("消费者取消接收消息");
		};
		System.out.println("c2等待接收消息");
		channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
	}
}
