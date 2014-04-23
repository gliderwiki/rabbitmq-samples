package org.gliderwiki.rabbitmq.application;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("rabbit-application-context.xml");
        AmqpTemplate amqpTemplate = context.getBean(AmqpTemplate.class);
        System.out.println("#### Consumer ####");
        System.out.println(amqpTemplate.receive("simplequeue"));
    }
}