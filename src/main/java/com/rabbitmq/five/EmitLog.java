package com.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Package:com.rabbitmq.five
 * @Auther:Brianwei
 * @date:2024/11/5:23:06
 * @discribe:
 */
public class EmitLog {

	public static final String EXCHANGENAME = "logs";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		channel.exchangeDeclare(EXCHANGENAME,"topic");
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String next = scanner.next();
			channel.basicPublish(EXCHANGENAME,"wei.*",null,next.getBytes());
		}
	}
}
