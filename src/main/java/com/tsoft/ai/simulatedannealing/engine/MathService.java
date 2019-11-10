package com.tsoft.ai.simulatedannealing.engine;

import java.util.Arrays;
import java.util.Random;

public class MathService {

    public int getRandom(int max) {
        return new Random().nextInt(max);
    }

    public int getRandom(int max, Integer ... excludedValues) {
        while (true) {
            int v = getRandom(max);

            if (Arrays.stream(excludedValues).noneMatch(a -> a == v)) {
                return v;
            }
        }
    }
}
