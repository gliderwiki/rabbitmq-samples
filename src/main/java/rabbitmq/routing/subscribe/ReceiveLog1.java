package rabbitmq.routing.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import rabbitmq.helloworld.common.RabbitmqClient;
import rabbitmq.routing.publish.LogDirectPublish;

import java.io.IOException;

public class ReceiveLog1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        // bind exchange to random queue.
        channel.exchangeDeclare(LogDirectPublish.EXCHANGE, LogDirectPublish.EXCHANGE_TYPE);
        String queue = channel.queueDeclare().getQueue();

        // bind for each routing key = severity
        channel.queueBind(queue, LogDirectPublish.EXCHANGE, "ERROR");

        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck  = false; // for redelivering when consuming failed.
        channel.basicConsume(queue, autoAck, consumer);

        System.out.println("### Ready to receive");

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            String message = new String(delivery.getBody());

            System.out.println("[x] Received '" + delivery.getEnvelope().getRoutingKey() + "' " + message+"'");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
