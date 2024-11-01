package com.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Package:com.rabbitmq.two
 * @Auther:Brianwei
 * @date:2024/10/31:20:46
 * @discribe:
 */
public class Producer1 {

	private static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < 100000; i++) {
			channel.basicPublish("",QUEUE_NAME,null,String.valueOf(i).getBytes());
		}
	}
}
