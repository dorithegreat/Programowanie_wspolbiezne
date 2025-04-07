import java.util.Random;

public class GeneratorRandom {
    public static void main(String[] args) {
        Random random = new Random();
        System.out.println(args[0]); //print n
        int n = Integer.parseInt(args[0]);
        System.out.println(random.nextInt(n) + 1); //print v
        for (int i = 0; i < n; i++) {
            System.out.println(random.nextInt(100));
        }
    }
}
