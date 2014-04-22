package rabbitmq.helloworld;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import rabbitmq.helloworld.common.RabbitmqClient;

import java.io.IOException;

/**
 * 메시지 받기.
 */
public class RecvMessage {
    public static final String QUEUE_NAME = SendMessage.QUEUE_NAME;

    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println(
                "[***] Waiting for Message."
        );

        QueueingConsumer consumer = new QueueingConsumer(channel);

        channel.basicConsume(QUEUE_NAME, true, consumer);
        System.out.println("# ready to receive ");

        int count = 0;
        while (true) {
            QueueingConsumer.Delivery  delivery = consumer.nextDelivery();
            String message  = new String(delivery.getBody());
            System.out.println(
                    "Received [" +count+ "] = " + message
            );
        }

    }
}
