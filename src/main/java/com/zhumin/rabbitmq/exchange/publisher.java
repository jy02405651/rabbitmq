package com.zhumin.rabbitmq.exchange;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
* 定义一个生产者（用到交换器来绑定队列、而不是直接通过队列交互）（订阅模式）
* @author 朱敏
* @date 2018/1/17 0017 17:21
*/
public class publisher {

    private static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory  factory= new ConnectionFactory();
        factory.setHost("192.168.1.239");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //指定交换器规则为分发模式
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //分发消息
        for(int i = 0 ; i < 5; i++){
            String message = "Hello World! " + i;
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }


}
