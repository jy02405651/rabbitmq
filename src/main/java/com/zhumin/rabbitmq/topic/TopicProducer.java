package com.zhumin.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
* 申明一个匹配模式(topic)消息发送者
* @author 朱敏
* @date 2018/1/18 0018 13:32
*/
public class TopicProducer {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] aregs) {
        Connection connection = null;
        Channel channel = null;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.239");

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            //声明一个匹配模式的交换器
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            //声明routekey
            String[] routingKeys = new String[]{"quick.orange.rabbit",
                    "lazy.orange.elephant",
                    "quick.orange.fox",
                    "lazy.brown.fox",
                    "quick.brown.fox",
                    "quick.orange.male.rabbit",
                    "lazy.orange.male.rabbit"};
            //发送消息
            for (String severity : routingKeys) {
                String message = "From " + severity + " routingKey' s message!";
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
                System.out.println("TopicSend [x] Sent '" + severity + "':'" + message + "'");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

}
