package com.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Package:com.rabbitmq.three
 * @Auther:Brianwei
 * @date:2024/11/1:21:37
 * @discribe:
 */
public class P {

	public static final String NAME = "ask";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		channel.queueDeclare(NAME,true,false,false,null);
		//开启发布确认
		channel.confirmSelect();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String next = scanner.next();
			channel.basicPublish("",NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,next.getBytes(StandardCharsets.UTF_8));
			System.out.println("生产者发送消息");
		}
	}
}
