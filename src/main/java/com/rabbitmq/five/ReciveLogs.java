package com.rabbitmq.five;

import com.rabbitmq.client.*;
import com.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Package:com.rabbitmq.five
 * @Auther:Brianwei
 * @date:2024/11/5:19:45
 * @discribe:
 */
public class ReciveLogs {

	public static final String EXCHANGENAME = "logs";


	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		channel.exchangeDeclare(EXCHANGENAME,"topic");
		//声明一个临时队列，当消费者断开连接时，队列就会消失
		String queue = channel.queueDeclare().getQueue();
		channel.queueBind(queue,EXCHANGENAME,"wei.*");
		//开启消息接收确认
		channel.confirmSelect();
		//消息异步确认
		ConcurrentSkipListMap<Long,String> map = new ConcurrentSkipListMap<>();
		ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
			if (multiple){
				ConcurrentNavigableMap<Long, String> yesMap = map.headMap(deliveryTag);
				yesMap.clear();
			}else {
				map.remove(deliveryTag);
			}
			System.out.println("异步确认成功");
		};
		ConfirmCallback nackCallback = (deliveryTag,multiple) -> {
			System.out.println("异步确认失败");
		};
		channel.addConfirmListener(ackCallback,nackCallback);
		System.out.println("等待接收消息");
		//消费者接收消息回调接口
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			System.out.println("接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
		};
		channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
	}
}
