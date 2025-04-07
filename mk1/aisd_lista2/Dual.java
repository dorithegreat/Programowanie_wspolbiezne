package com.sorting;

import java.util.Scanner;

public class Dual {

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

  
    static void sort(int[] array, int low, int high) { 
        if (low < high) { 
            int[] pivot; 
            pivot = partition(array, low, high); 
            
            sort(array, low, pivot[0] - 1); 
            sort(array, pivot[0] + 1, pivot[1] - 1); 
            sort(array, pivot[1] + 1, high); 
        } 
    } 
  
    static int[] partition(int[] array, int low, int high) 
    { 
        if (array[low] > array[high]){ // pivots are the two opposite ends of the array
            swap(array, low, high);   // so if they are in the wrong order we need to swap them
        } 
        keyComparisons++;

        int j = low + 1; 
        int g = high - 1;
        int k = low + 1; 
        int p = array[low];
        int q = array[high]; 
            
        while (k <= g) { 
            
            //smaller that first pivot
            if (array[k] < p) { 
                swap(array, k, j); 
                j++; 
            } 
            
            //between the two pivots
            else if (array[k] >= q) { 
                while (array[g] > q && k < g) {
                    g--; 
                }
                    
                swap(array, k, g); 
                g--; 
                
                if (array[k] < p) 
                { 
                    swap(array, k, j); 
                    j++; 
                } 
                keyComparisons++;
            } 
            keyComparisons++;
            k++; 
        } 
        j--; 
        g++; 
        
        // Bring pivots to their appropriate positions. 
        swap(array, low, j); 
        swap(array, high, g); 
    
        return new int[] { j, g }; //an array of the two pivots because functions only return one value
    }

    static void swap(int[] array, int i, int j) { 
        int temp = array[i]; 
        array[i] = array[j]; 
        array[j] = temp; 
        keySwaps++;
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
