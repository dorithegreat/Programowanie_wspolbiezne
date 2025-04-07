package com.sorting;

import java.io.FileWriter;

import org.apache.commons.math3.random.MersenneTwister;

public class InsertionTest {

    private static int keyComparisons = 0;
    private static int keySwaps = 0;

    private static FileWriter writer;
    private static MersenneTwister random = new MersenneTwister();

    public static void main(String[] args) {

        try {
            writer = new FileWriter("insertion.txt");
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        int k = 100;
        for (int n = 10; n <= 50; n += 10) {
            for (int i = 0; i < k; i++) {
                int[] array = new int[n];
                for (int j = 0; j < n; j++) {
                    array[j] = random.nextInt(2 * n - 1);
                }
                sort(array);
                    try {
                        writer.write(((Integer)n).toString() + " ");
                        writer.write(((Integer)keyComparisons).toString() + " ");
                        writer.write(((Integer)keySwaps).toString() + "\n");
                        writer.flush();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    keyComparisons = 0;
                    keySwaps = 0;
            }   
        }
    }

    private static int[] sort (int[] array){
        int key = 0;
        for (int i = 1; i < array.length; i++) {
            key = array[i];
            int j = i -1;
            while (j >= 0 && array[j] > key) {
                keyComparisons++;
                array[j + 1] = array[j];
                keySwaps++;
                j = j -1;
            }
            keyComparisons++;
            array[j + 1] = key;
        }
        return array;
    }

    public static void print(int[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }
}
