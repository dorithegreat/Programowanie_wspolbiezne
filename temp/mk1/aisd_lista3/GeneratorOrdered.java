import java.util.Random;

public class GeneratorOrdered {
    public static void main(String[] args) {
        Random random = new Random();
        System.out.println(args[0]);
        int n = Integer.parseInt(args[0]);
        System.out.println(random.nextInt(n));
        for (int i = 0; i < n; i++) {
            System.out.println(i);
        }
    }
}
