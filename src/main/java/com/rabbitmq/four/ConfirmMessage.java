package com.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.three.P;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Package:com.rabbitmq.four
 * @Auther:Brianwei
 * @date:2024/11/4:17:14
 * @discribe:
 */
public class ConfirmMessage {

	private final static Integer MESSAGECOUNT = 1000;
	private final static String QUEUE = "confirm";

	public static void main(String[] args) throws Exception {
		//消息单个确认，单个确认用时：728
		publishMessageIndividually();
		//批量确认,批量确认用时：53
		publishMessageBatch();
		//异步确认，异步确认用时：11
		publishMessageAsync();
	}


	//消息单个确认
	private static void publishMessageIndividually() throws Exception {
		Channel channel = RabbitMqUtil.getChannel();
		channel.queueDeclare(QUEUE,true,false,false,null);
		channel.confirmSelect();
		long l = System.currentTimeMillis();
		for (Integer i = 0; i < MESSAGECOUNT; i++) {
			channel.basicPublish("",QUEUE,null,String.valueOf(i).getBytes(StandardCharsets.UTF_8));
			boolean b = channel.waitForConfirms();
			if (b){
				System.out.println("消息发送成功");
			}
		}
		long l1 = System.currentTimeMillis();
		long l2 = l1 - l;
		System.out.println("单个确认用时：" + l2);
	}

	//消息批量确认,弊端：无法确认哪条消息没有保存
	private static void publishMessageBatch() throws Exception {
		Channel channel = RabbitMqUtil.getChannel();
		channel.queueDeclare(QUEUE,true,false,false,null);
		channel.confirmSelect();
		long l = System.currentTimeMillis();
		for (Integer i = 0; i < MESSAGECOUNT; i++) {
			channel.basicPublish("",QUEUE,null,String.valueOf(i).getBytes(StandardCharsets.UTF_8));
		}
		channel.confirmSelect();
		long l1 = System.currentTimeMillis();
		long l2 = l1 - l;
		System.out.println("批量确认用时：" + l2);
	}

	//异步发布确认
	private static void publishMessageAsync() throws Exception {
		Channel channel = RabbitMqUtil.getChannel();
		channel.queueDeclare(QUEUE,true,false,false,null);
		channel.confirmSelect();
		ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();
		//multiple 是否一次性确认多个消息
		ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
			if (multiple){
				//消息发布成功，删除map里存的消息，剩下的就是失败的消息
				//headmap获取的是 map 的一部分内容，并不是一个新的map
				ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap = map.headMap(deliveryTag);
				longStringConcurrentNavigableMap.clear();
			}else {
				map.remove(deliveryTag);
			}
			System.out.println("异步确认成功");
		};
		ConfirmCallback nackCallback = (deliveryTag,multiple) -> {
			System.out.println("异步确认失败");
		};
		channel.addConfirmListener(ackCallback,nackCallback);
		long l = System.currentTimeMillis();
		for (Integer i = 0; i < MESSAGECOUNT; i++) {
			String s = String.valueOf(i);
			channel.basicPublish("",QUEUE,null,s.getBytes(StandardCharsets.UTF_8));
			//此处记录下所有要发的消息的总和
			map.put(channel.getNextPublishSeqNo(),s);
		}
		long l1 = System.currentTimeMillis();
		long l2 = l1 - l;
		System.out.println("异步确认用时：" + l2);
	}
}
