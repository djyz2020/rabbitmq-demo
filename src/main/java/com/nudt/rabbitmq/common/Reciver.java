package com.nudt.rabbitmq.common;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

/**
 * Created by haibozhang on 2018/12/5.
 */
public class Reciver extends DefaultConsumer {
    private Channel channel;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     * @param channel the channel to which this consumer is attached
     */
    public Reciver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        //super.handleDelivery(consumerTag, envelope, properties, body);
        String messageBody = new String(body);
        System.out.println("消费者收到的消息：" + messageBody);
        // 签收消息，确认消息
        // envelope.getDeliveryTag() 获取这个消息的TagId
        // false 只确认接受当前的消息，设置为true的时候则代表签收该消费者所有未签收的消息
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
