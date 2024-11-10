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
public class C1 {

	public static final String NORMALEXCHANGE = "normalexchange";
	public static final String DEADEXCHANGE = "deadexchange";
	public static final String NORMALQUEUE = "normalqueue";
	public static final String DEADQUEUE = "deadqueue";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		//声明死信和普通交换机
		channel.exchangeDeclare(NORMALEXCHANGE, BuiltinExchangeType.DIRECT);
		channel.exchangeDeclare(DEADEXCHANGE, BuiltinExchangeType.DIRECT);
		//声明普通队列
		Map<String, Object> map = new HashMap<>();
		//正常队列设置死信交换机
		map.put("x-dead-letter-exchange",DEADEXCHANGE);
		//设置死信routingkey
		map.put("x-dead-letter-routing-key","lisi");
		map.put("x-max-length",6);
		channel.queueDeclare(NORMALQUEUE,false,false,false,map);
		//声明死信队列
		channel.queueDeclare(DEADQUEUE,false,false,false,null);
		//绑定交换机与队列
		channel.queueBind(DEADQUEUE,DEADEXCHANGE,"lisi");
		channel.queueBind(NORMALQUEUE,NORMALEXCHANGE,"zhangsan");
		System.out.println("开始接收消息");
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			String s = new String(message.getBody(), StandardCharsets.UTF_8);
			if (s.equals("info4")){
				System.out.println("此消息被拒绝");
				//false表示不把消息放回普通队列
				channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
			}else {
				System.out.println("c1收到消息：" + s);
			}
		};
		channel.basicConsume(NORMALQUEUE,false,deliverCallback,tag -> {});
	}
}
