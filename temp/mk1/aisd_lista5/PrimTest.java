import java.util.Set;

public class PrimTest {
    public static void main(String[] args) {
        int nMin = 10;
        int nMax = 1000;
        int step = 10;
        int rep = 10;

        for (int n = nMin; n <= nMax; n += step) {
            for (int m = 0; m < rep; m++) {
                Graph graph = new Graph();
                graph.generate(n);

                final long primStartTime = System.currentTimeMillis();
                Set set = graph.prim();
                final long primEndTime = System.currentTimeMillis();

                final long kruskalStartTime = System.currentTimeMillis();
                Set set2 = graph.kruskal();
                final long kruskalEndTime = System.currentTimeMillis();

                System.out.println(n + " " + m + " " + (primEndTime - primStartTime) + " " + (kruskalEndTime - kruskalStartTime));
            }
        }
    }
}
