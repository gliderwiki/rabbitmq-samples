package rabbitmq.topics.publish;

import com.rabbitmq.client.Channel;
import rabbitmq.helloworld.common.Message;
import rabbitmq.helloworld.common.RabbitmqClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TopicsPublish {

    public static String EXCHANGE = "topic_logs";
    public static String EXCHANGE_TYPE = "topic";

    public static void main(String[] args) throws IOException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE);

        // create messages
        List<Message> messageList = new ArrayList<>();
        // Message 에 exchange 명을 넣어주어야 돌아간다.
        messageList.add(new Message(EXCHANGE, "quick.orange.rabbit", "m1"));
        messageList.add(new Message(EXCHANGE, "lazy.orange.elephant", "m2"));
        messageList.add(new Message(EXCHANGE, "quick.orange.fox", "m3"));
        messageList.add(new Message(EXCHANGE, "lazy.brown.fox", "m4"));
        messageList.add(new Message(EXCHANGE, "lazy.pink.rabbit", "m5"));
        messageList.add(new Message(EXCHANGE, "quick.brown.fox", "m6"));
        messageList.add(new Message(EXCHANGE, "quick.orange.male.rabbit", "m7"));
        messageList.add(new Message(EXCHANGE, "lazy.orange.male.rabbit", "m8"));
        messageList.add(new Message(EXCHANGE, "lazy", "m9"));

        System.out.println("## Ready to send");

        for (Message message : messageList) {
            // send message with routing key
            channel.basicPublish(message.exchange, message.routingKey, null, message.body.getBytes());

            System.out.println(" [x] Sent " + message.toString());
        }

        client.close();
    }
}
