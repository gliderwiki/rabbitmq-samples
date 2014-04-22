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
 *   producer에서 해야 할 일은 브로커(exchange)와 연결하고, 메시지를 보낼 큐를 설정(또는 큐를 생성)한 뒤 메시지를 브로커로 보내면 된다.
 *  Q에 메시지를 삽입하는 Producer - 로직을 P와 C로 분리하여, 오래 걸리는 작업은 C가 담당하면 사용자 입장에서의 응답속도가 빨라질 것이다.
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {

        /**
         * create a connection to the server
         * 물리적인 TCP Connection, 보안이 필요할 경우 TLS(SSL) Connection을 사용할 수 있음.
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        /**
         * we must declare a queue for us to send to
         * 하나의 물리적인 connection 내에 생성되는 가상 논리적인 connection들.
         * Consumer의 process나 thread는 각자 이 channel을 통해서 queue에 연결 될 수 있다.
         */
        Channel channel = connection.createChannel();


        /**
         * then we can publish a message to the queue
         * Queue를 만드는 것을 declare라고 하며, 애플리케이션 코드에서도 쉽게 만들 수 있다.
         * 만약 해당 큐가 이미 존재하고 있다면, 다시 queue를 만들지 않고, queue가 없을 경우에만 만든다.
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
