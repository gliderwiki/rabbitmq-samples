package org.gliderwiki.rabbitmq.amqp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Create a template of the tutorial bean defined in the XML file and send 10 message
 * to the TUTORIAL-EXCHANGE configured in the rabbt-listener-contet.xml file with the routing key
 * "my.routingkey.1"
 */
public class TutorialSender {

    public static void main(String[] args) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("rabbit-sender-context.xml");//loading beans
        AmqpTemplate aTemplate = (AmqpTemplate) context.getBean("tutorialTemplate");// getting a reference to the sender bean

        for (int i = 0; i < 10; i++) {
            aTemplate.convertAndSend("my.routingkey.1", "Message # " + i + " on " + new Date());// send
        }
    }
}
//end of TutorialSender

