package com.nudt.rabbitmq.workqueue;

import com.nudt.rabbitmq.utils.RabbitConstants;
import com.nudt.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.*;
import java.io.IOException;

/**
 * Created by haibozhang on 2018/12/16.
 */
public class SMSSender {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConstants.QUEUE_SMS, false ,false, false, null);
        channel.basicQos(1); //同一时刻服务器只会发送一条消息给消费者, 处理完一个（MQ确认后）取一个, 优化服务器消费数据
        //绑定队列到交换器
        channel.queueBind(RabbitConstants.QUEUE_SMS, "test_exchange_fanout", ""); //不设置路由键
        //channel.queueBind(RabbitConstants.QUEUE_SMS, "test_exchange_direct", "update");  //匹配路由键为update
        //channel.queueBind(RabbitConstants.QUEUE_SMS, "test_exchange_direct", "delete");  //匹配路由键是delete
        // topic交换机: 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。
        // 因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”
        //channel.queueBind(RabbitConstants.QUEUE_SMS, "test_exchange_topic", "item.#");     //使用item.# 匹配所有的以item开头的, "."为分隔匹配符
        channel.basicConsume(RabbitConstants.QUEUE_SMS, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                String messageBody = new String(body);
                System.out.println("消费者收到的消息：" + messageBody);
                // 签收消息，确认消息
                // envelope.getDeliveryTag() 获取这个消息的TagId
                // false 只确认接受当前的消息，设置为true的时候则代表签收该消费者所有未签收的消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });

    }

}
