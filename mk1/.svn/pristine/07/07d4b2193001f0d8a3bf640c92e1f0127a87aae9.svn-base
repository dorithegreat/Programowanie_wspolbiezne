package com.sorting;

import java.util.Scanner;

public class Insertion {

    private static int keyComparisons = 0;
    private static int keySwaps = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }
        System.out.print("to sort: ");
        print(array);
        sort(array);
        System.out.print("after sorting: ");
        print(array);
        System.out.println(keyComparisons + " comparisons, " + keySwaps + " swaps");
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
