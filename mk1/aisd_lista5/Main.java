import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        int n = 5;
        graph.generate(n);
        
        System.out.println("n = " + n);
        System.out.println("Prim's algorithm:");
        System.out.println(Arrays.toString(graph.prim().toArray()));
        System.out.println("Kruskal's algorithm: ");
        System.out.println(Arrays.toString(graph.kruskal().toArray()));

        System.out.println("Turns in which each node will get the message:");
        HashMap<GraphNode, Integer> map = graph.propagation(graph.getSomeNode());
        for (Map.Entry<GraphNode, Integer> node : map.entrySet()) {
            System.out.println(node.getKey().getName() + " " + node.getValue());
        }
    }
}
