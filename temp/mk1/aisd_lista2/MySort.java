package com.sorting;

import java.io.FileWriter;

import org.apache.commons.math3.random.MersenneTwister;

public class MySort {

    private static int keyComparisons = 0;
    private static int keySwaps = 0;

    private static FileWriter writer;
    private static MersenneTwister random = new MersenneTwister();

    public static void main(String[] args) {
        try {
            writer = new FileWriter("my.txt");
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
                mySort(array, 0, n - 1);
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
                mySort(array, 0, n - 1);
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

        // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    public static void merge(int arr[], int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        int left[] = new int[n1];
        int right[] = new int[n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i){
            left[i] = arr[l + i];
        }
        for (int j = 0; j < n2; ++j){
            right[j] = arr[m + 1 + j];
        }

        // Merge the temp arrays

        // Initial indices of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
            }
            else {
                arr[k] = right[j];
                j++;
            }
            keyComparisons++;
            keySwaps++;
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            arr[k] = left[i];
            i++;
            k++;
            keySwaps++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            arr[k] = right[j];
            j++;
            k++;
            keySwaps++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    public static void mergeSort(int arr[], int l, int r)
    {
        if (l < r) {

            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    public static void mySort(int arr[], int l, int r)
    {
        if (l < r) {

            // Find the middle point
            int m = l + (r - l) / 2;
            int pivot = m;
            while (pivot < r && arr[pivot + 1] > arr[pivot]) {
                pivot++;
                keyComparisons++;
            }
            if (pivot == r) { //m is in the middle of the last sequence
                pivot = m;
                while (pivot > l && arr[pivot] > arr[pivot - 1]) {
                    pivot--;
                }
                if (pivot == l) { //whole subtable is ordered, nothing to sort
                    return;
                }
                keyComparisons++;
            }
            else { //if there is another sequence after m we need to sort the second half too
                keyComparisons++;
                mySort(arr, pivot + 1, r);
            }

            mySort(arr, l, pivot); //in both cases, sort the first part of the table
            
            // Merge the sorted halves
            merge(arr, l, pivot, r);
        }
    }

    public static void print(int[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }
}
