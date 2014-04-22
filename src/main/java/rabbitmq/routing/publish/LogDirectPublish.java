package rabbitmq.routing.publish;

import com.rabbitmq.client.Channel;
import rabbitmq.helloworld.common.Message;
import rabbitmq.helloworld.common.RabbitmqClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogDirectPublish {
    public static String EXCHANGE = "direct_logs";  // exchange name
    public static String EXCHANGE_TYPE = "direct";  // exchange type

    public enum SEVERITY {
        INFO, WARNING, ERROR
    }

    public static void main(String[] args) throws IOException {
        RabbitmqClient  client = new RabbitmqClient();
        Channel channel  = client.getChannel();

        channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE);

        // create messages
        List<Message> messageList = new ArrayList<>();

        messageList.add(new Message(EXCHANGE, SEVERITY.INFO.name(), "start server"));
        messageList.add(new Message(EXCHANGE, SEVERITY.ERROR.name(), "send error!"));
        messageList.add(new Message(EXCHANGE, SEVERITY.WARNING.name(), "disk space left only 10%"));
        messageList.add(new Message(EXCHANGE, SEVERITY.INFO.name(), "stop server"));

        // send message with routing key = severity
        System.out.println("## Ready to send");
        for (Message message : messageList) {
            channel.basicPublish(message.exchange, message.routingKey, null, message.body.getBytes());

            System.out.println("[x] Sent : " + message.toString());

        }

        client.close();
    }

}
