package org.learnings.statemachines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties
public class ServiceApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServiceApplication.class, args);
        debug(context);
    }

    private static void debug(ApplicationContext ctx) {
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println("-------> " + beanName);
        }
    }
}
