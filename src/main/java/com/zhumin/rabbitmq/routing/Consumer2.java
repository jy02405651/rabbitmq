package com.zhumin.rabbitmq.routing;


import com.rabbitmq.client.*;

import java.io.IOException;

/**
* 定义消费者(直连模式:绑定路由收取error,warn,log级别信息)
* @author 朱敏
* @date 2018/1/18 0018 11:33
*/
public class Consumer2 {

    // 交换器名称
    private static final String EXCHANGE_NAME = "direct_logs";
    // 路由关键字
    private static final String[] routingKeys = new String[]{"error"};

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.239");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //匿名队列
        String queueName = channel.queueDeclare().getQueue();
        //根据routekey绑定路由
        for (String severity : routingKeys) {
            channel.queueBind(queueName,EXCHANGE_NAME,severity);
            System.out.println("ReceiveLogsDirect2 exchange:"+EXCHANGE_NAME+", queue:"+queueName+", BindRoutingKey:" + severity);
        }
        System.out.println("ReceiveLogsDirect2 [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);



    }

    }
