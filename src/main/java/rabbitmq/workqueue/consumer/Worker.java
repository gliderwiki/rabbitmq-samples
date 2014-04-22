package rabbitmq.workqueue.consumer;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import rabbitmq.helloworld.common.RabbitmqClient;
import rabbitmq.workqueue.producer.NewTask;

import java.io.IOException;

public class Worker {
    private static final String QUEUE_NAME = NewTask.QUEUE_NAME;

    public static void main(String[] args) throws IOException, InterruptedException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        boolean durable = true;

        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [***] Waiting for Messages.");

        int prefetchCount = 1;

        channel.basicQos(prefetchCount);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        boolean autoAck = false; // for redelivering when consuming failed.
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);

        System.out.println("## Ready to receive.");

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            String message = new String(delivery.getBody());

            System.out.println(" [x] Received : '" + message + "'");
            doWork(message);
            System.out.println(" [x] Done");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
