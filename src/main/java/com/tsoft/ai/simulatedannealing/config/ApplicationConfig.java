package com.tsoft.ai.simulatedannealing.config;

import com.tsoft.ai.simulatedannealing.Application;
import com.tsoft.ai.simulatedannealing.engine.MathService;
import com.tsoft.ai.simulatedannealing.engine.SimulatedAnnealingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class ApplicationConfig {

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Bean
    public SimulatedAnnealingService simulatedAnnealingService() {
        return new SimulatedAnnealingService(mathService(), applicationProperties());
    }

    @Bean
    public MathService mathService() {
        return new MathService();
    }

    @Bean
    public Application application() {
        return new Application(simulatedAnnealingService());
    }
}
