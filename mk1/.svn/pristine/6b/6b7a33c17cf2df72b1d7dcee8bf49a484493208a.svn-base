package com.sorting;

import java.io.FileWriter;

import org.apache.commons.math3.random.MersenneTwister;

public class HybridTest {
    private static int keyComparisons = 0;
    private static int keySwaps = 0;

    private static FileWriter writer;
    private static MersenneTwister random = new MersenneTwister();

    public static void main(String[] args) {

        try {
            writer = new FileWriter("hybrid.txt");
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
                sort(array, 0, n - 1);
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
        for (int n = 1000; n <= 50000; n += 1000) {
            for (int i = 0; i < k; i++) {
                int[] array = new int[n];
                for (int j = 0; j < n; j++) {
                    array[j] = random.nextInt(2 * n - 1);
                }
                sort(array, 0, n - 1);
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

    public static void sort(int[] array, int p, int q){
        if (p >= 0 && q >= 0 && p < q) {
            if (q - p < 10) {
                insertion(array, p, q); 
                return;
            }
            //System.out.print("sorting from " + p + " to " + q + ": ");
            //print(array);
            int pivot = partition(array, p, q);
            partition(array, p, q);
            sort(array, p, pivot);
            sort(array, pivot + 1, q);
        }
    }

    public static int partition(int[] array, int p, int q){
        int pivot = array[(p + q) / 2];
        int i = p - 1;
        int j = q + 1;
        //print(array);
        while (true) {
            do {
                i++;
                keyComparisons++;
            } while (array[i] < pivot);

            do {
                j--;
                keyComparisons++;
            } while (array[j] > pivot);

            if (i >= j) {
                return j;
            }

            int pom = array[i];
            array[i] = array[j];
            array[j] = pom;
            keySwaps++;
            //print(array);
        } 
    }

    public static void insertion(int[] array, int p, int q){
        //System.out.print("switching to insertion on: ");
        //print(array);
        int key = 0;
        for (int i = p; i < q; i++) {
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
    }
}
