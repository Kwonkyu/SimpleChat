package com.example.simplechat.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonMessage {

    private String name;

    public PersonMessage(String name) {
        this.name = name;
    }

}
