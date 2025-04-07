import java.util.ArrayList;
import java.util.Random;

public class BstMain {
    public static void main(String[] args) {
        BstTree tree = new BstTree();
        ArrayList values = new ArrayList<Integer>();
        Random random = new Random();

        int n = 50;
        for (int i = 0; i < n; i++) {
            int number = random.nextInt(2*n -1);
            tree.insert(number);
            values.add(number);
            System.out.println("insert " + number);
            tree.print();
        }

        for (int i = 0; i < n; i++) {
            int index = random.nextInt(values.size());
            int number = (int)values.get(index);
            System.out.println("delete " + number);
            tree.delete(number);
            values.remove(index);
            tree.print();
        }
    }
}
