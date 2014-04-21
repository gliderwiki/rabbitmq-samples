package rabbitmq.helloworld.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 *  RabbitMQ is a message broker.
 *  In essence, it accepts messages from producers, and delivers them to consumers.
 *  In-between, it can route, buffer, and persist the messages according to rules you give it.
 *
 *  The sender will connect to RabbitMQ, send a single message, then exit.
 *  Q에 메시지를 삽입하는 Producer - 로직을 P와 C로 분리하여, 오래 걸리는 작업은 C가 담당하면 사용자 입장에서의 응답속도가 빨라질 것이다.
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {

        /**
         * create a connection to the server
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        /**
         * we must declare a queue for us to send to
         */
        Channel channel = connection.createChannel();


        /**
         * then we can publish a message to the queue
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");


        /**
         * close the channel and the connection
         */
        channel.close();
        connection.close();
    }

}
