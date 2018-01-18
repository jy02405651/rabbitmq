package com.zhumin.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
* 消息生产者
* @author 朱敏
* @date 2018/1/17 0017 9:30
*/
public class Producer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String args[]) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //mq地址
        factory.setHost("192.168.1.238");
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发送消息到队列
        String message = "Hello Rabbit";
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
        //关闭通道
        channel.close();
        //关闭连接
        connection.close();
    }

}
