package rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import rabbitmq.helloworld.common.Message;
import rabbitmq.helloworld.common.RabbitmqClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * publish / subscribe
 */
public class Publish {
    public static String EXCHANGE = "logs";
    public static String EXCHANGE_TYPE = "fanout";


    public static void main(String[] args) throws IOException {
        RabbitmqClient client = new RabbitmqClient();

        Channel channel = client.getChannel();

        channel.exchangeDeclare(EXCHANGE, EXCHANGE_TYPE);

        List<Message> messageList = new ArrayList<>();

        for (int i = 0; i < 11 ; i++) {
            messageList.add(new Message(EXCHANGE, "", "fanout [" + i + "]"));
        }

        System.out.println(
                "## Ready to Publish"
        );


        for (Message msg : messageList) {
            channel.basicPublish(msg.exchange, msg.routingKey, null, msg.body.getBytes());

            System.out.println(" [x] Sent  " + msg.toString());

            try {
                Thread.sleep(1000);
            } catch ( InterruptedException e) {
                e.printStackTrace();
            }

        }

        client.close();

    }
}
