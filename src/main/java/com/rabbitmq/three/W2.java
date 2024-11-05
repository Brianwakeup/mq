package com.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Package:com.rabbitmq.three
 * @Auther:Brianwei
 * @date:2024/11/1:22:57
 * @discribe:
 */
public class W2 {

	public static final String NAME = "ask";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		System.out.println("c2等待接收消息处理时间较短");
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			//沉睡30s
			try {
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			String m = new String(message.getBody(), StandardCharsets.UTF_8);
			System.out.println("接收到消息：" + m);
			//消息标记tag，false代表只应答接收到的那个传递的消息，true为应答所有消息包括传递过来的消息
			channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
		};
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("线程二接收消息失败：" + consumerTag);
		};
		//设置不公平分发消息,1代表不公平，0代表公平
		channel.basicQos(2);
		//消费消息，手动应答
		channel.basicConsume(NAME,false,deliverCallback,cancelCallback);
	}
}
