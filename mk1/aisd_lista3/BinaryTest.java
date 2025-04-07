import java.util.Random;

public class BinaryTest {
    public static void main(String[] args) {
        Random random = new Random();
        for (int n = 1000; n <= 100000; n += 1000) {
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = i;
            }
            for (int m = 0; m < 10; m++) {
                BinarySearch.comparisons = 0;
                System.out.println(m + " " + n + " " + BinarySearch.binarySearch(array, 0, n - 1, -1) + " " + BinarySearch.comparisons);
            }
        }
    }
}
