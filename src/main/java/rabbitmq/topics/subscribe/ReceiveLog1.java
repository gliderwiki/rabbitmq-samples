package rabbitmq.topics.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import rabbitmq.helloworld.common.RabbitmqClient;
import rabbitmq.topics.publish.TopicsPublish;

import java.io.IOException;

public class ReceiveLog1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        // bind exchange to random queue.
        channel.exchangeDeclare(TopicsPublish.EXCHANGE, TopicsPublish.EXCHANGE_TYPE);
        String queue = channel.queueDeclare().getQueue();

        // bind for each routing key (severity)
        String routingKey = "*.orange.*";
        channel.queueBind(queue, TopicsPublish.EXCHANGE, routingKey);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck = false; // for redelivering when consuming failed.

        channel.basicConsume(queue, autoAck, consumer);

        System.out.println("## Ready to receive, routingKey = " + routingKey);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            String message = new String(delivery.getBody());
            String receiveRoutingKey = delivery.getEnvelope().getRoutingKey();

            System.out.println(" [x] Received '"
                    + receiveRoutingKey + "': '" + message
                    + "'");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        }
    }
}
