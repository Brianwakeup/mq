package com.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Package:com.rabbitmq.six
 * @Auther:Brianwei
 * @date:2024/11/6:1:26
 * @discribe:
 */
public class ReciveLogsTopic01 {

	public static final String EXCHANGENAME = "topic_logs";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		channel.exchangeDeclare(EXCHANGENAME, BuiltinExchangeType.TOPIC);
		String queueName = "Q1";
		channel.queueDeclare(queueName,false,false,false,null);
		String routingkey = "*.orange.*";
		channel.queueBind(queueName,EXCHANGENAME,routingkey);
		System.out.println("开始接收消息");
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			System.out.println("接收到消息" + new String(message.getBody(), StandardCharsets.UTF_8));
			System.out.println("接收消息的队列：" + queueName + "，绑定键：" + routingkey);
		};
		channel.basicConsume(queueName,true, deliverCallback, tag -> {});
	}
}
