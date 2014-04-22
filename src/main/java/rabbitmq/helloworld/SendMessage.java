package rabbitmq.helloworld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import rabbitmq.helloworld.common.Message;
import rabbitmq.helloworld.common.RabbitmqClient;

public class SendMessage {

    public static final String QUEUE_NAME = "SENDER";

    public static void main(String[] args) throws IOException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        // create message
        List<Message> messageList = new ArrayList<Message>();

        for(int i = 0; i < 10; i++) {
            messageList.add(new Message(QUEUE_NAME, "Hello World [" +i+"]"));
        }

        System.out.println("## Ready to Send");

        int index = 0;
        for (Message msg : messageList) {
            index++;
            channel.basicPublish(msg.exchange, msg.routingKey, null, msg.body.getBytes());
            System.out.println("Sent ... [" + index + "] = " + msg.toString());

        }
        client.close();
    }
}
