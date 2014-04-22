package rabbitmq.workqueue.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import rabbitmq.helloworld.common.Message;
import rabbitmq.helloworld.common.RabbitmqClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NewTask {
    public static final String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException {
        RabbitmqClient client = new RabbitmqClient();
        Channel channel = client.getChannel();

        // durable 속성 : 메세지를 디스크에 저장. memory에 저장하는 것은 transient라고 한다.
        boolean durable = true;

        /**
         * Queue를 만드는 것을 declare라고 하며, 애플리케이션 코드에서도 쉽게 만들 수 있다.
         * 만약 해당 큐가 이미 존재하고 있다면, 다시 queue를 만들지 않고, queue가 없을 경우에만 만든다.
         */
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        
        // create messages
        System.out.println("## Create Message !");
        List<Message> messageList = new ArrayList<>();
        for(int i = 0; i < 10 ;  i++) {
            messageList.add(new Message(QUEUE_NAME, "task [" + i + "]" + repeatChar(".", i)));
        }

        System.out.println("## Ready to Send");

        for(Message m : messageList) {
            channel.basicPublish(m.exchange, m.routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, m.body.getBytes());
            System.out.println(" [x] Sent : " + m.toString());
        }

        client.close();
    }

    private static String repeatChar(String ch, int repeat) {
        String str = "";
        for (int i = 0; i < repeat; i++) {
            str += ch;
        }
        return str;
    }
}
