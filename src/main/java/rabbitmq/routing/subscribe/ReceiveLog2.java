package rabbitmq.routing.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import rabbitmq.helloworld.common.RabbitmqClient;
import rabbitmq.routing.publish.LogDirectPublish;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Yion
 * Date: 14. 4. 22
 * Time: 오후 8:19
 * To change this template use File | Settings | File Templates.
 */
public class ReceiveLog2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        channel.exchangeDeclare(LogDirectPublish.EXCHANGE, LogDirectPublish.EXCHANGE_TYPE);

        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue, LogDirectPublish.EXCHANGE, "INFO");
        channel.queueBind(queue, LogDirectPublish.EXCHANGE, "WARNING");

        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck = false; // for redeliverying when consuming failed.
        channel.basicConsume(queue, autoAck, consumer);

        System.out.println("$$$ Ready to receive");

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());

            System.out.println(" [x] Received '"
                + delivery.getEnvelope().getRoutingKey() + "' ' " + message + "'"
            );

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
