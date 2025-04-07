import java.util.Scanner;

public class Select {
    public static int SIZE_OF_SUBARRAYS = 5;
    public static int comparisons = 0;
    public static int swaps = 0;

    public static void main(String[] args) {
        comparisons = 0;
        swaps = 0;

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

    private static int medianOfSubarray(int[] array, int p, int q){
        insertion(array, p, q);
        System.out.print("found median: ");
        print(array);
        return array[(q - p) / 2 + p];
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

    public static int select(int[] array, int p, int q, int i){
        if (p == q) {
            return array[p];
        }

        int[] medians;
        if (q - p % SIZE_OF_SUBARRAYS == 0) {
            medians = new int[(q - p) / SIZE_OF_SUBARRAYS];
        }
        else{
            medians = new int[(q - p) / SIZE_OF_SUBARRAYS + 1];
        }

        int position = 0;
        for(int j = p; j < q; j += SIZE_OF_SUBARRAYS){
            if (q - j < SIZE_OF_SUBARRAYS) {
                medians[position] = medianOfSubarray(array, j, (q - p) - 1);
            }
            else{
                medians[position] = medianOfSubarray(array, j, j + SIZE_OF_SUBARRAYS - 1);
            }
            comparisons++;
            position++;
        }
        int pivot = select(medians, 0, medians.length - 1, medians.length / 2);
        int r = partition(array, p, q, pivot);
        //System.out.println("median of medians: " + array[r]);

        int k = r - p + 1;
        if (k ==i) {
            comparisons++;
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

    public static int partition(int arr[], int low, int high, int pivot) 
    {
        for (int i = low; i <= high; i++) {
            if (arr[i] == pivot) { 
                arr[i]=arr[high]; 
                arr[high]=pivot;  
                swaps++;
            }
        }
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
        
        System.out.println("partition with pivot " + pivot + ": ");
        // print(arr);
        return i+1;
    }

    public static void print(int[] array){
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }
}
