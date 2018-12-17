package com.nudt.rabbitmq.workqueue;

import com.nudt.rabbitmq.utils.RabbitConstants;
import com.nudt.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by haibozhang on 2018/12/16.
 */
public class SMSSender02 {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConstants.QUEUE_SMS, false ,false, false, null);
        channel.basicQos(1); //处理完一个取一个
        channel.basicConsume(RabbitConstants.QUEUE_SMS, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String messageBody = new String(body);
//                try {
//                    Thread.sleep(600);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println("消费者收到的消息：" + messageBody);
                // 签收消息，确认消息
                // envelope.getDeliveryTag() 获取这个消息的TagId
                // false 只确认接受当前的消息，设置为true的时候则代表签收该消费者所有未签收的消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

    }

}
