package com.sorting;

import java.util.Scanner;

public class Hybrid {

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
        String original = convertToString(array);
        sort(array, 0, array.length - 1);
        System.out.println("\noriginal array: " + original);
        System.out.print("after sorting: ");
        print(array);
        System.out.println(keyComparisons + " comparisons, " + keySwaps + " swaps");
    }

    public static void sort(int[] array, int p, int q){
        if (p >= 0 && q >= 0 && p < q) {
            if (q - p < 4) {
                insertion(array, p, q); 
                return;
            }
            System.out.print("sorting from " + p + " to " + q + ": ");
            print(array);
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
        System.out.print("switching to insertion on: ");
        print(array);
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
            keySwaps++;
        }
    }

    public static void print(int[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }

    public static String convertToString(int[] array){
        String conversion = "";
        for (int i : array) {
            conversion += " ";
            conversion += i;
        }
        return conversion;
    }
}
