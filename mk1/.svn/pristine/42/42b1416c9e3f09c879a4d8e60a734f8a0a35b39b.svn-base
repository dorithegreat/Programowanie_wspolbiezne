import java.util.Random;


public class SelectTest {
    public static void main(String[] args) {
            Random random = new Random();
    
            Select.SIZE_OF_SUBARRAYS = 3;
            for (int m = 0; m < 50; m++) {
                for (int n = 100; n <= 5000; n += 100) {
                    int[] array = new int[n];
                    for (int j = 0; j < n; j++) {
                        array[j] = random.nextInt(100);
                    }
    
                    for (int i = 1; i <= 5; i++) {
                        // k = i * (n / 5)
                        int[] array2 = array.clone();
                        Select.comparisons = 0;
                        Select.swaps = 0;
                        System.out.println(m + " " + n + " " + i + " " + Select.select(array2, 0, n - 1, i * (n / 5)) + " " + Select.comparisons + " " + Select.swaps);
                    }
                }
            }
        }
}
