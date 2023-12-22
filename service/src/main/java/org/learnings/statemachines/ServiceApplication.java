package org.learnings.statemachines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.statemachine.config.EnableStateMachine;

@SpringBootApplication
@EnableConfigurationProperties
@EnableStateMachine
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
