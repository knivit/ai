package com.tsoft.ai.simulatedannealing.engine;

import lombok.Data;

@Data
public class SolutionStep {

    private int timer;
    private double temperature;
    private double energy;
    private double worseAcceptedPercent;

}
