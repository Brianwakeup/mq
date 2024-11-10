package com.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package:com.rabbitmq.seven
 * @Auther:Brianwei
 * @date:2024/11/8:17:26
 * @discribe:
 */
public class C2 {

	public static final String NORMALEXCHANGE = "normalexchange";
	public static final String DEADEXCHANGE = "deadexchange";
	public static final String NORMALQUEUE = "normalqueue";
	public static final String DEADQUEUE = "deadqueue";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		System.out.println("开始接收消息");
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			System.out.println("c2收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
		};
		channel.basicConsume(DEADQUEUE,true,deliverCallback,tag -> {});
	}
}
