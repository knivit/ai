package com.tsoft.ai.simulatedannealing;

import com.tsoft.ai.simulatedannealing.config.ApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Application application = context.getBean(Application.class);
        application.execute();
    }
}
