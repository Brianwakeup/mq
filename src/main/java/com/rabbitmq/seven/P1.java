package com.rabbitmq.seven;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;

/**
 * @Package:com.rabbitmq.seven
 * @Auther:Brianwei
 * @date:2024/11/10:12:03
 * @discribe:
 */
public class P1 {

	public static final String NORMALEXCHANGE = "normalexchange";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		//死信消息，构建ttl
//		AMQP.BasicProperties properties = new AMQP.BasicProperties()
//				.builder()
//				.expiration("10000")
//				.build();
		for (int i = 1; i < 11; i++) {
			String message = "info" + i;
			channel.basicPublish(NORMALEXCHANGE,"zhangsan",null,message.getBytes());
		}
	}
}
