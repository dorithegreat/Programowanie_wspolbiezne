import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class Graph {
    private List<GraphNode> nodes = new ArrayList<GraphNode>();
    private List<GraphEdge> edges = new ArrayList<GraphEdge>();
    private Random random = new Random();
    private Map<GraphNode, ArrayList<GraphEdge>> adjacent = new HashMap<GraphNode, ArrayList<GraphEdge>>();

    public void generate(int n){
        for (int i = 0; i < n; i++) {
            GraphNode node = new GraphNode(i);
            nodes.add(node);
            
            for (GraphNode graphNode : nodes) {
                if (graphNode != node) {
                    edges.add(new GraphEdge(node, graphNode, random.nextDouble()));
                }
            }
        }

        for (GraphNode node : nodes) {
            ArrayList<GraphEdge> adjacentEdges = new ArrayList<>();
            for (GraphEdge edge : edges) {
                if (edge.contains(node)) {
                    adjacentEdges.add(edge);
                }
            }
            adjacent.put(node, adjacentEdges);
        }
    }

    //returns first node it can find
    //it should be the one named 0 but doesn't technically have to be
    public GraphNode getSomeNode(){
        return nodes.get(0);
    }

    public Set<GraphEdge> prim(){
        // Map<GraphNode, GraphEdge> map = new HashMap<GraphNode, GraphEdge>();

        Set<GraphNode> chosenNodes = new HashSet<GraphNode>();
        Set<GraphNode> leftoverNodes = new HashSet<GraphNode>();
        Set<GraphEdge> solution = new HashSet<GraphEdge>();

        for (GraphNode graphNode : nodes) {
            leftoverNodes.add(graphNode);
        }
        chosenNodes.add(nodes.get(0));
        leftoverNodes.remove(nodes.get(0));

        while (!leftoverNodes.isEmpty()) {
            GraphEdge bestEdge = null;
            double currentMinimum = 2.0; //definitely bigger than maximum weight in the graph
            GraphNode nextNode = null;
            for (GraphEdge graphEdge : edges) {
                if (chosenNodes.contains(graphEdge.getFirstNode()) ^ chosenNodes.contains(graphEdge.getSecondNode())) {
                    if (currentMinimum > graphEdge.getWeight()) {
                        bestEdge = graphEdge;
                        currentMinimum = graphEdge.getWeight();
                        if (chosenNodes.contains(graphEdge.getFirstNode()) && !chosenNodes.contains(graphEdge.getSecondNode())) {
                            nextNode = graphEdge.getSecondNode();
                        }
                        else if (chosenNodes.contains(graphEdge.getSecondNode()) && !chosenNodes.contains(graphEdge.getFirstNode()) ) {
                            nextNode = graphEdge.getFirstNode();
                        }
                    }
                }
            }
            chosenNodes.add(nextNode);
            leftoverNodes.remove(nextNode);
            solution.add(bestEdge);
        }

        for (GraphNode node : nodes) {
            ArrayList<GraphEdge> adjacentEdges = new ArrayList<>();
            for (GraphEdge edge : solution) {
                if (edge.contains(node)) {
                    adjacentEdges.add(edge);
                }
            }
            adjacent.put(node, adjacentEdges);
        }

        return solution;
    }

    public Set<GraphEdge> kruskal(){
        EdgeComparator edgeComparator = new EdgeComparator();
        TreeMap<GraphEdge, Double> map = new TreeMap<GraphEdge, Double>(edgeComparator); //a sorted map
        for (GraphEdge graphEdge : edges) {
            map.put(graphEdge, graphEdge.getWeight());

        }

        Set<GraphNode> chosenNodes = new HashSet<GraphNode>();
        Set<GraphNode> leftoverNodes = new HashSet<GraphNode>();
        Set<GraphEdge> solution = new HashSet<GraphEdge>();

        for (GraphNode graphNode : nodes) {
            leftoverNodes.add(graphNode);
        }

        //take n - 1 best edges that don't create cycles
        for (int i = 0; i < nodes.size() - 1; i++) {
            GraphEdge bestEdge = map.firstEntry().getKey();

            //skip those edges that create a cycle
            while (chosenNodes.contains(bestEdge.getFirstNode()) && chosenNodes.contains(bestEdge.getSecondNode())) {
                map.remove(bestEdge);
                if (!map.isEmpty()) {
                    bestEdge = map.firstEntry().getKey();
                    break;
                }
            }

            if (map.isEmpty()) {
                break;
            }
            chosenNodes.add(bestEdge.getFirstNode());
            chosenNodes.add(bestEdge.getSecondNode());
            leftoverNodes.remove(bestEdge.getFirstNode());
            leftoverNodes.remove(bestEdge.getSecondNode());
            solution.add(bestEdge);
            Double removed = map.remove(bestEdge);
        }

        return solution;
    }


    public HashMap<GraphNode, Integer> bfs(GraphNode v){
        ArrayDeque<GraphNode> queue = new ArrayDeque<GraphNode>();
        // Set<GraphNode> visited = new HashSet<GraphNode>();
        HashMap<GraphNode, Integer> visited = new HashMap<GraphNode, Integer>();
        visited.put(v, 0);
        queue.add(v); //puts at the end
        while (!queue.isEmpty()) {
            v = queue.pollFirst();
            for (GraphEdge edge : adjacent.get(v)) {
                GraphNode other = edge.getOtherNode(v);
                if (!visited.containsKey(other)) {
                    visited.put(other, visited.get(v) + 1);
                    queue.add(other);
                }
            }
        }
        return visited;
    }

    public HashMap<GraphNode, Integer> propagation(GraphNode v){
        HashMap<GraphNode, Integer> bfs =  bfs(v);
        // Set visited = new HashSet<GraphNode>();

        int max = 0;
        for (Map.Entry<GraphNode, Integer> bfsNode : bfs.entrySet()) {
            if (bfsNode.getValue() > max) {
                max = bfsNode.getValue();
            }
        }

        //assigns each node the size of its subtree basically
        Map<GraphNode, Integer> dynamic = new HashMap<GraphNode, Integer>();
        int i = max;
        while (i >= 0) {
            //for each node of a specific level
            for (Map.Entry<GraphNode, Integer> entry : bfs.entrySet()) {
                if (entry.getValue() == i) {
                    GraphNode node = entry.getKey();
                    int value = 0;
                    //for every node that is adjacent to it and lower in the tree
                    for (GraphEdge edge : adjacent.get(node)) {
                        GraphNode other = edge.getOtherNode(node);
                        if (bfs.get(other) > entry.getValue()) {
                            value += dynamic.get(other) + 1;
                        }
                    }
                    dynamic.put(node, value);
                }
            }
            i--;
        }

        //run bfs again assigning each node the number of turn when it will receive the message
        ArrayDeque<GraphNode> queue = new ArrayDeque<GraphNode>();
        HashMap<GraphNode, Integer> visited = new HashMap<GraphNode, Integer>();
        visited.put(v, 1);
        queue.add(v); //puts at the end
        while (!queue.isEmpty()) {
            v = queue.pollFirst();
            int offset = 1;
            ArrayList<GraphEdge> adjacents = adjacent.get(v);
            //weird trick workaround because using my custom comparator inexcusably didn't work
            final GraphNode u = v;
            // System.out.println(adjacents.toString());
            // System.out.println(dynamic.toString());
            Collections.sort(adjacents, new Comparator<GraphEdge>() {
                @Override
                public int compare(GraphEdge arg0, GraphEdge arg1) {
                    int i1 = dynamic.get(arg0.getOtherNode(u));
                    int i2 = dynamic.get(arg1.getOtherNode(u));
                    int i = Integer.compare(i1, i2);
                    // System.out.println(i1 + " " + i2 + " " + i);
                    return i;
                }
            }.reversed());
            // System.out.println(adjacents.toString());
            for (int index = 0; index < adjacents.size(); index++) {
                GraphEdge edge = adjacents.get(index);
                GraphNode other = edge.getOtherNode(v);
                if (!visited.containsKey(other)) {
                    visited.put(other, visited.get(v) + offset);
                    offset++;
                    queue.add(other);
                }
            }
        }
        return visited;
    }
}
