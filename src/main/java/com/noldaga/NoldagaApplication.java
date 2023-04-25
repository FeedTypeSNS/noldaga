package com.noldaga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NoldagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoldagaApplication.class, args);
    }

}
