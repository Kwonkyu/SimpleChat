package com.example.simplechat.controller;

import com.example.simplechat.controller.bind.PostCreationNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NoticeController {

    // message url used by client or other message publishers.
    // should be used with application destination prefix.
    @MessageMapping("/post/centerA")
    // send return value to message broker! broker's prefix is set by enableSimpleBroker.
    @SendTo("/notice/{writerName}")
    public PostCreationNotification postCreated(String title) throws InterruptedException {
        // payload of message is mapped to PersonMessage.
        Thread.sleep(1000); // mock delay time for processing message. async!
        return new PostCreationNotification(title);
        // need sanitization on message to return client.
    }

}
