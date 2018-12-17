package com.nudt.rabbitmq.common;

import com.nudt.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * Created by haibozhang on 2018/12/5.
 */
public class Consumer {

    public static void consumer(){
        Connection conn = RabbitUtils.getConnection();
        Channel channel = null;
        try {
            // 创建"通信"通道，相当于TCP中的虚拟连接
            channel = conn.createChannel();
            // 创建队列，声明并创建一个队列，如果队列已存在，则使用这个队列
            // 第一个参数： 队列名称
            // 第二个参数： 是否持久化，false对应不持久化数据，MQ停掉数据就会丢失
            // 第三个参数： 是否队列私有化，false则代表所有消费者都可以访问，true代表只有第一次拥有它的消费者才能一直使用，其它消费者不让访问
            // 第四个参数： 队列是否自动删除，false代表停掉后不自动删除队列
            // 其它额外的参数： null
            channel.queueDeclare("helloworld", false, false, false, null);
            // 创建一个消息消费者
            // 第一个参数： 队列名称
            // 第二个参数： 代表是否自动确认收到消息，false代表手动编程来确认消息，这是MQ的推荐做法
            channel.basicConsume("helloworld", false, new Reciver(channel));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           //RabbitUtils.releaseResource(conn, channel);
        }
    }

    public static void main(String[] args) {
        consumer();
    }

}

