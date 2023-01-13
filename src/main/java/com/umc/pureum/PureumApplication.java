package com.umc.pureum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PureumApplication {

    public static void main(String[] args) {
        SpringApplication.run(PureumApplication.class, args);
    }

}
