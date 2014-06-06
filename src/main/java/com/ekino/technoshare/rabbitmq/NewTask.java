package com.ekino.technoshare.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "work_queue"; // Contract

    public static void main(String[] argv) throws Exception {
        // Connection + Channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declare the queue
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null); // TO-EXPLAIN Durability

        // Create the message and publish it
        for (String message : argv) {
            channel.basicPublish( "", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes()); // TO-EXPLAIN Default Exchange
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
    }

}