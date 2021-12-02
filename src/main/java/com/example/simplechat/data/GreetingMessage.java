package com.example.simplechat.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GreetingMessage {

    private String greeting;

    public GreetingMessage(String greeting) {
        this.greeting = greeting;
    }

}
