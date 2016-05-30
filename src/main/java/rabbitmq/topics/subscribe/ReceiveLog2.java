package rabbitmq.topics.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import rabbitmq.helloworld.common.RabbitmqClient;
import rabbitmq.topics.publish.TopicsPublish;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Yion
 * Date: 14. 4. 22
 * Time: 오후 9:02
 * To change this template use File | Settings | File Templates.
 */
public class ReceiveLog2 {

    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        // bind exchange to random queue.
        channel.exchangeDeclare(TopicsPublish.EXCHANGE, TopicsPublish.EXCHANGE_TYPE);
        String queue = channel.queueDeclare().getQueue();

        // bind for each routing key (severity)
        String routingKey1 = "lazy.#";
        String routingKey2 = "*.*.rabbit";

        channel.queueBind(queue, TopicsPublish.EXCHANGE, routingKey1);
        channel.queueBind(queue, TopicsPublish.EXCHANGE, routingKey2);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck = false; // for redelivering when consuming failed.

        channel.basicConsume(queue, autoAck, consumer);

        System.out.println("## Ready to receive, routingKey = " + routingKey1);
        System.out.println("## Ready to receive, routingKey = " + routingKey2);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            String message = new String(delivery.getBody());

            System.out.println(" [xx] Received '"
                    + delivery.getEnvelope().getRoutingKey() + "' : '" + message
                    + "'");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        }
    }
}
