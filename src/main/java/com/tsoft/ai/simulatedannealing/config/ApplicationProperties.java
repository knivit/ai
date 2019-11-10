package com.tsoft.ai.simulatedannealing.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class ApplicationProperties {

    @Value("${max.length}")
    private int maxLength;

    @Value("${initial.temperature}")
    private double initialTemperature;

    @Value("${final.temperature}")
    private double finalTemperature;

    @Value("${alpha:0.98}")
    private double alpha;

    @Value("${steps.per.change}")
    private int stepsPerChange;

}
