package com.ekino.technoshare.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

public class Worker {

    private static final String TASK_QUEUE_NAME = "work_queue"; // Contract

    public static void main(String[] argv) throws Exception {
        // Connection + Channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1); // TO-EXPLAIN Round robin

        // Declare the queue
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null); // TO-EXPLAIN Durability
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // Register a consumer
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

        // Main loop
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            System.out.println(" [x] Received '" + message + "'");
            doWork(message);
            System.out.println(" [x] Done");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false); // TO-EXPLAIN Ack
        }
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }

}
