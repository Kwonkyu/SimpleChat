package com.example.simplechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SimplechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplechatApplication.class, args);
    }

}
