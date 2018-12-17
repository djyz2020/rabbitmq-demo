package com.nudt.rabbitmq.workqueue;

import com.nudt.rabbitmq.entity.SMS;
import com.nudt.rabbitmq.utils.RabbitConstants;
import com.nudt.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;

/**
 * Created by haibozhang on 2018/12/16.
 */
public class OrderSystem {

    public static void main(String[] args) throws IOException {
        Connection connnection = RabbitUtils.getConnection();
        Channel channel = connnection.createChannel();
        channel.queueDeclare(RabbitConstants.QUEUE_SMS, false, false, false, null);
        //声明交换器
        //channel.exchangeDeclare("test_exchange_fanout", "fanout"); // fanout交换器
        //channel.exchangeDeclare("test_exchange_direct", "direct"); // fanout交换器
        //channel.exchangeDeclare(("test_exchange_topic","topic");
        for(int i = 0; i < 1000000; i++){
            SMS sms = new SMS("乘客" + i, "1390000" + i, "您的车票已预订成功！");
            channel.basicPublish("", RabbitConstants.QUEUE_SMS, null, sms.toString().getBytes());
            //channel.basicPublish("test_exchange_fanout", RabbitConstants.QUEUE_SMS, null, sms.toString().getBytes());
        }
        // 释放资源
        RabbitUtils.releaseResource(connnection, channel);
    }

}
