import java.util.ArrayList;
import java.util.Random;


public class RbTest {
    public static void main(String[] args) {
        Random random = new Random();
        RbTree tree2 = new RbTree();
        tree2.insert(2);
        // System.out.println(tree2.height() + " " + tree2.keyComparisons + " " + tree2.repins);
        
        for (int m = 0; m < 5; m++) {
            BstTree tree = new BstTree();
            tree.keyComparisons = 0;
            tree.repins = 0;
            
            for (int n = 10000; n <= 80000; n += 10000) {
                ArrayList values = new ArrayList<Integer>();

                int number = 0;
                int heights = 0;
                int maxHeight = 0;
                for (int i = 0; i < n; i++) {
                    number = random.nextInt(2 * n - 1);
                    values.add(number);
                    tree.insert(number);
                    int height = tree.height();
                    if (height > maxHeight) {
                        maxHeight = height;
                    }
                    heights += height;
                }
                int index = 0;
                for (int i = 0; i < n; i++) {
                    index = random.nextInt(values.size());
                    tree.delete((int)values.get(index));
                    heights += tree.height();
                }
                System.out.println(m + " " + n + " " + tree.keyComparisons + " " + tree.repins + " " + heights / (2 * n) + " " + maxHeight);
            }
            
        }
    }
}
