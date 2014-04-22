package rabbitmq.helloworld.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * receiver is pushed messages from RabbitMQ
 * it running to listen for messages and print them out.
 * 큐로부터 메시지를 전달받을 consumer를 작성한다.
 * consumer가 할 일은 브로커와 연결을 설정하고 메시지를 받아올 큐를 설정한 뒤 메시지를 받아와 처리하는 것이다.
 */
public class Receive {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * open a connection and a channel, and declare the queue from which we're going to consume
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel  = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [***] Waiting for messages.");

        /**
         * QueueingConsumer.nextDelivery() blocks until another message has been delivered from the server.
         */
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }
}
