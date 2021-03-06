package com.zhumin.rabbitmq.queues;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
* 创建消费者C1
* @author 朱敏
* @date 2018/1/17 0017 16:04
*/
public class Consumer1 {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.239");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //第二个参数定义true声明消息持久化
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("Worker1 [*] Waiting for messages. To exit press CTRL+C");
        // 每次从队列中获取数量
        channel.basicQos(1);

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println("Worker1 [x] Received '" + message + "'");
                try {
                    //模拟业务逻辑处理
                    doWork(message);
                } finally {
                    System.out.println("Worker1 [x] Done");
                    // 消息处理完成确认
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        // 消息消费完成确认
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

    }

    private static void doWork(String task) {
        try {
            Thread.sleep(1000); // 暂停1秒钟

            //为了测试消费者1在运行的时候异常的情况 故写此代码；
/*            int[] i ={1,2,3};
            int q = i[4];*/
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }

}
