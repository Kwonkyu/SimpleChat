package com.example.simplechat.controller;

import com.example.simplechat.data.GreetingMessage;
import com.example.simplechat.data.PersonMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/greeting") // message is sent to /greeting, this handler called.
    @SendTo("/publish/greetings") // return value of this method is delivered to /publish/greetings.
    public GreetingMessage greet(PersonMessage message) throws InterruptedException {
        // payload of message is mapped to PersonMessage.
        Thread.sleep(1000); // mock delay time for processing message. async!
        return new GreetingMessage(String.format("Hello, %s", message.getName()));
        // need sanitization on message to return client.
    }

}
