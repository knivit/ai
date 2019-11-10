package com.tsoft.ai.simulatedannealing.engine;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class MemberType implements Cloneable {

    private int[] solutionType;

    @Getter
    @Setter
    private double energy;

    public MemberType(int length) {
        solutionType = new int[length];
        energy = 0.0d;
    }

    public void swap(int x, int y) {
        int temp = solutionType[x];
        solutionType[x] = solutionType[y];
        solutionType[y] = temp;
    }

    public void set(int i, int v) {
        solutionType[i] = v;
    }

    public int get(int i) {
        return solutionType[i];
    }

    @Override
    public MemberType clone() {
        try {
            MemberType clone = (MemberType)super.clone();
            clone.solutionType = Arrays.copyOf(solutionType, solutionType.length);
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
