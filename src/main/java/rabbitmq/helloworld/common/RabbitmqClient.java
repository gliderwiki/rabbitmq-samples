package rabbitmq.helloworld.common;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;

/**
 * RabbitMQ 서버에 접속, 종료를 처리하는 공통루틴
 */
public class RabbitmqClient {
    private static String HOST = "127.0.0.1";
    private Connection connection = null;
    private Channel channel = null;

    public Channel getChannel() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitmqClient.HOST);
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();

        return this.channel;
    }

    public void close() throws IOException {

        if(this.connection.isOpen()) {
            this.connection.close();
        }
        if(this.channel.isOpen()) {
            this.channel.close();
        }

    }
}
