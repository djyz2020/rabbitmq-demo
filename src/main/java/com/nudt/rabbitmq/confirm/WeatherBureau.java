package com.nudt.rabbitmq.confirm;

import com.nudt.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by haibozhang on 2018/12/17.
 */
public class WeatherBureau {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
               System.out.println("消息已被Broker接收，Tag：" + deliveryTag);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息已被Broker拒收，Tag：" + deliveryTag);
            }
        });
        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return r) {
                System.err.println("Return编码：" + r.getReplyCode() + ", Return描述：" + r.getReplyText());
            }
        });
        channel.basicPublish("exchange_weather", "key1", true, null, "生产者消息".getBytes());
        //RabbitUtils.releaseResource(connection, channel);
    }

}
