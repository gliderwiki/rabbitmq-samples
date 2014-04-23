package org.gliderwiki.rabbitmq.application;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Producer {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("rabbit-application-context.xml");
        AmqpTemplate amqpTemplate = context.getBean(AmqpTemplate.class);

        System.out.println("##  ##");
        System.out.println("##  ##");
        System.out.println("##########");
        System.out.println("######  ##");
        System.out.println("##########");

        System.out.println("#### Producer ####");

        amqpTemplate.convertAndSend("simplequeue", "Hello World");
    }
}