package rabbitmq.fanout.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import rabbitmq.fanout.publish.Published;
import rabbitmq.helloworld.common.RabbitmqClient;

import java.io.IOException;

public class Subscribed {

    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitmqClient client  = new RabbitmqClient();
        Channel channel = client.getChannel();

        // bind exchange to random queue.
        channel.exchangeDeclare(Published.EXCHANGE, Published.EXCHANGE_TYPE);

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, Published.EXCHANGE, "");

        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck = false;    // for redelivering when consuming failed.

        channel.basicConsume(queue, autoAck, consumer);

        System.out.println("## Ready to receive.");

        while (true) {
            QueueingConsumer.Delivery delivery  = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            System.out.println("[x] Received '" + message + "'");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        }
    }
}
