package com.zhumin.rabbitmq.queues;


import com.rabbitmq.client.*;

import java.io.IOException;

/**
* 创建生产者发布多个消息演示工作队列
* @author 朱敏
* @date 2018/1/17 0017 15:55
*/
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws java.io.IOException, Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.239");
        Connection connection = factory.newConnection();
/*        connection.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException e) {
                System.out.println("Connection error:"+e);
            }
        });*/

        Channel channel = connection.createChannel();
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        //消息异步回传:NIO异步
        //确认方式：1、异步ConfirmListener；2、同步waitForConfirms
        //ConfirmListener、waitForConfirms均需要配合confirm机制使用
        //异步实现发送消息的确认(此部分的消息确认是指发送消息到队列，并非确认消息的有效消费)
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long l, boolean b) throws IOException {
                //multiple：测试发现multiple随机true或false，原因未知
                System.out.println("Ack deliveryTag:"+l+",multiple:"+b);
            }

            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("NAck deliveryTag:"+l+",multiple:"+b);
            }
        });

        //发布5条消息
        for(int i=0;i<20;i++){
            String message = "Hello World! " + i;
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();

    }


}
