package org.gliderwiki.rabbitmq.amqp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TutorialListenerContainer {
    public static void main(String[] args) {
        ApplicationContext c1 = new ClassPathXmlApplicationContext("rabbit-listener-context.xml");
    }
}