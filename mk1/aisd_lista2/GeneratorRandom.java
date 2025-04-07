package com.sorting;

import org.apache.commons.math3.random.MersenneTwister;

public class GeneratorRandom {
    public static void main(String[] args) {
        MersenneTwister random = new MersenneTwister();
        System.out.println(args[0]);
        int n = Integer.parseInt(args[0]);
        for (int i = 0; i < n; i++) {
            System.out.println(random.nextInt(2 * n - 1));
        }
    }
}
