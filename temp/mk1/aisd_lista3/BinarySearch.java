import java.util.Scanner;

public class BinarySearch {
    public static int comparisons;

    public static void main(String[] args) {
        comparisons = 0;
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int v = scanner.nextInt();
        System.out.println("looking for " + v);
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }
        Select.print(array);
        // int[] array = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        // int n = 10;
        // int v = 9;
        System.out.println(binarySearch(array, 0, n - 1, v));
        System.out.println(comparisons + " comparisons");
    }

    public static boolean binarySearch(int[] array, int p, int q, int k){
        if (p > q) {
            return false;
        }
        int n = (q - p) / 2 + p;
        comparisons++;
        if (array[n] == k) {
            return true;
        }
        else if (array[n] > k) {
            return binarySearch(array, p, n - 1, k);
        }
        else{
            return binarySearch(array, n + 1, q, k);
        }
    }
}
