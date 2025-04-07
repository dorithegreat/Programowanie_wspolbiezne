import java.util.Random;
import java.util.Scanner;

public class RandomizedSelect {
    public static int swaps = 0;
    public static int comparisons = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }

        // int[] array = new int[]{36, 9, 40, 59, 8, 10, 46};
        // int k = 5;
        // int n = 2;

        System.out.println("k = " + k);
        System.out.print("before: ");
        print(array);
        System.out.println();

        System.out.println("found number: " + select(array, 0, array.length - 1, k));
        System.out.print("\nafter select: ");
        print(array);
        System.out.println();
        System.out.println("sorted: ");
        insertion(array, 0, array.length - 1);
        print(array);
        System.out.println("k = " + k);
        System.out.println(comparisons + " comparisons, " + swaps + " swaps");

        // scanner.close();
        // System.out.println(select(new int[]{36, 9, 40, 59, 8, 10, 46}, 0, 6, 5));
    }

    public static int select(int[] array, int p, int q, int i){
        if (p == q) {
            return array[p];
        }
        int r  = partition(array, p, q);
        int k = r - p + 1;
        if (i == k) {
            comparisons += 1;
            return array[r];
        }
        else if (i < k) {
            comparisons += 2;
            return select(array, p, r - 1, i);
        }
        else{
            comparisons += 2;
            return select(array, r + 1, q, i - k);
        }
    }
    
    
    static void random(int arr[],int low,int high) 
    { 
     
        Random rand= new Random(); 
        int pivot = rand.nextInt(high-low)+low; 
         
        int temp1=arr[pivot];  
        arr[pivot]=arr[high]; 
        arr[high]=temp1; 
        swaps ++;
    } 
     

    static int partition(int arr[], int low, int high) 
    { 
        // pivot is chosen randomly 
        random(arr,low,high);
        int pivot = arr[high]; 
     
 
        int i = (low-1); // index of smaller element 
        for (int j = low; j < high; j++) 
        { 
            // If current element is smaller than or 
            // equal to pivot 
            if (arr[j] < pivot) 
            { 
                i++; 
 
                // swap arr[i] and arr[j] 
                int temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
                swaps++;
            } 
            comparisons++;
        } 
 
        // swap arr[i+1] and arr[high] (or pivot) 
        int temp = arr[i+1]; 
        arr[i+1] = arr[high]; 
        arr[high] = temp; 
        swaps++;
        print(arr);
        return i+1;
    }

    public static void insertion(int[] array, int p, int q){
        int key = 0;
        for (int i = p; i <= q; i++) {
            key = array[i];
            int j = i -1;
            while (j >= p && array[j] > key) {
                comparisons++;
                array[j + 1] = array[j];
                swaps++;
                j = j -1;
            }
            array[j + 1] = key;
        }
    }

    public static void print(int[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }
}