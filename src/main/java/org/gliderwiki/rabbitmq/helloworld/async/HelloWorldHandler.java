package org.gliderwiki.rabbitmq.helloworld.async;

public class HelloWorldHandler {

    public void handleMessage(String text) {
        System.out.println("Received: " + text);
    }

}