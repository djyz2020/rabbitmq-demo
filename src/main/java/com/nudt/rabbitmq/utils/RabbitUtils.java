package com.nudt.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by haibozhang on 2018/12/16.
 */
public class RabbitUtils {

    public static ConnectionFactory connectionFactory = new ConnectionFactory();

    static {
        connectionFactory.setHost("192.168.106.130");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/test");

//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("test");
//        connectionFactory.setPassword("test");
//        connectionFactory.setVirtualHost("/test");
    }

    public static Connection getConnection(){
        try {
            return connectionFactory.newConnection();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    public static void releaseResource(Connection conn, Channel channel){
        try {
            if(channel != null){
                channel.close();
            }
            if(conn != null){
                conn.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
