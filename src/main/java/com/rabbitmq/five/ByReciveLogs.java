package com.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.utils.RabbitMqUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Package:com.rabbitmq.five
 * @Auther:Brianwei
 * @date:2024/11/5:21:07
 * @discribe:
 */
public class ByReciveLogs {

	public static final String EXCHANGENAME = "logs";

	//将获取到的消息存储到磁盘
	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtil.getChannel();
		channel.exchangeDeclare(EXCHANGENAME,"fanout");
		String queue = channel.queueDeclare().getQueue();
		channel.queueBind(queue,EXCHANGENAME,"");
		System.out.println("等待接收消息，将接收到的消息写入到文件");
		DeliverCallback deliverCallback = (consumerTag,message) ->{
			//将消息写入到文件
			String s = new String(message.getBody(), StandardCharsets.UTF_8);
			File file = new File("C:\\Users\\BrianWei\\Desktop\\message.txt");
			FileUtils.writeStringToFile(file,s,StandardCharsets.UTF_8);
			System.out.println("数据写入文件成功");
			//应答
			channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
		};
		channel.basicConsume(queue,false,deliverCallback,tag -> {});
	}
}
